package br.com.mpr.image.service

import br.com.mpr.image.utils.toObject
import br.com.mpr.image.vo.DimensionVo
import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.MetadataException
import com.drew.metadata.exif.ExifIFD0Directory
import com.drew.metadata.jpeg.JpegDirectory
import com.google.gson.reflect.TypeToken
import java.awt.Color
import java.awt.Image
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.system.measureTimeMillis

class ImageService{

    /**
     * Redimenciona um File image mantendo a proporcao pelo comprimento.
     */
    fun resize(image: File, width: Int, imageResult : File){
        val sourceImage = ImageIO.read(image)
        val thumbnail = sourceImage.getScaledInstance(width, -1, Image.SCALE_SMOOTH)
        val bufferedThumbnail = BufferedImage(thumbnail.getWidth(null),
                thumbnail.getHeight(null),
                BufferedImage.TYPE_INT_RGB)
        bufferedThumbnail.graphics.drawImage(thumbnail, 0, 0, null)
        ImageIO.write(bufferedThumbnail, "jpg", imageResult)

    }

    /**
     * Redimenciona uma BufferedImage mantendo a proporcao da largura da imagem.
     * Devolve o BufferedImage redimencionado
     */
    fun resizeByWidth(sourceImage: BufferedImage, width: Int ): BufferedImage{
        val thumbnail = sourceImage.getScaledInstance(width, -1, Image.SCALE_SMOOTH)
        val bufferedThumbnail = BufferedImage(thumbnail.getWidth(null),
                thumbnail.getHeight(null),
                BufferedImage.TYPE_INT_RGB)
        bufferedThumbnail.graphics.drawImage(thumbnail, 0, 0, null)
        return bufferedThumbnail
    }

    /**
     * Redimenciona um BufferedImage mantendo a proporcao da altura da imagem.
     * Devolve o BufferedImage redimencionado
     */
    fun resizeByHeight(sourceImage: BufferedImage, height: Int ): BufferedImage{
        val thumbnail = sourceImage.getScaledInstance(-1, height, Image.SCALE_SMOOTH)
        val bufferedThumbnail = BufferedImage(thumbnail.getWidth(null),
                thumbnail.getHeight(null),
                BufferedImage.TYPE_INT_RGB)
        bufferedThumbnail.graphics.drawImage(thumbnail, 0, 0, null)
        return bufferedThumbnail
    }

    fun merge(filesPictures: Array<File>, frameImage: File, imageResult: File){
        var frame = ImageIO.read(frameImage)
        val dimensions = getTransparentDimensionOfMetadata(frameImage)

        if (filesPictures.size != dimensions.size)
            throw IllegalArgumentException("Quantidade de fotos (${filesPictures.size}) não compativel " +
                    "com a quantiade de espaços (${dimensions.size}).")

        var pictures = reviseOrientation(filesPictures)
        // create the new image, canvas size is the max. of both image sizes
        //val w = Math.max(backImage.width, frontImage.width)
        //val h = Math.max(backImage.height, frontImage.height)
        val w = frame.width
        val h = frame.height
        val combined = BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)

        // paint both images, preserving the alpha channels
        val g = combined.graphics

        for (i in 0 until pictures.size){
            var pic = pictures[i]
            var dimen = dimensions[i]
            println( "${dimen} - IsPortrat = ${dimen.isPortrait()}")
            if ((dimen.isPortrait() && !imagePortrait(pic)) ||
                (!dimen.isPortrait() && imagePortrait(pic)) ) {
                pic = rotate(pic)
            }
            //ajustando o tamanho da foto para o tamanho da transparencia

            pic = if (dimen.isPortrait()){
                resizeByWidth(pic,(dimen.maxWidth - dimen.minWidth)+10)
            }else{
                resizeByHeight(pic,(dimen.maxHeight - dimen.minHeight)+10)
            }

            val widthMargin =  (dimen.maxWidth - dimen.minWidth - pic.width) / 2
            val heightMargin = (dimen.maxHeight - dimen.minHeight - pic.height) / 2

            //adicionando uma margem na foto do cliente
            g.drawImage(pic, dimen.minWidth + widthMargin, dimen.minHeight + heightMargin, null)
            g.drawImage(frame, 0, 0, null)

            // Save as new image


        }
        val finalImage = cleanTransparent(combined)
        ImageIO.write(finalImage, "PNG", imageResult)

    }




    fun merge (backImageFile: File, frontImageFile: File, imageResult: File){
        var backImage = reviseOrientation(backImageFile)
        var frontImage = ImageIO.read(frontImageFile)

        if(!imagePortrait(backImage)){
            frontImage = rotate(frontImage)

        }
        var dimension = getTransparentDimension(frontImage)
        backImage = resizeByHeight(backImage,(dimension.maxHeight - dimension.minHeight)+20)

        // create the new image, canvas size is the max. of both image sizes
        //val w = Math.max(backImage.width, frontImage.width)
        //val h = Math.max(backImage.height, frontImage.height)
        val w = frontImage.width
        val h = frontImage.height
        val combined = BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)

        // paint both images, preserving the alpha channels
        val g = combined.graphics


        val widthMargin =  (dimension.maxWidth - dimension.minWidth - backImage.width) / 2
        val heightMargin = (dimension.maxHeight - dimension.minHeight - backImage.height) / 2

        //adicionando uma margem na foto do cliente
        g.drawImage(backImage, dimension.minWidth + widthMargin, dimension.minHeight + heightMargin, null)
        g.drawImage(frontImage, 0, 0, null)

        val imageFinal = cleanTransparent(combined)

        // Save as new image
        ImageIO.write(imageFinal, "PNG", imageResult)
    }




    fun rotate(image: BufferedImage):BufferedImage{
        val width = image.width
        val height = image.height
        val newImage = BufferedImage(height, width, image.type)

        for (i in 0 until width)
            for (j in 0 until height)
                newImage.setRGB(height - 1 - j, i, image.getRGB(i, j))

        return newImage
    }

    fun cleanTransparent(image: BufferedImage):BufferedImage{
        val width = image.width
        val height = image.height

        for (x in 0 until width)
            for (y in 0 until height)
                if (image.getRGB(x, y) == 0) {
                    image.setRGB(x, y, Color.WHITE.rgb)
                }


        return image
    }

    private fun imagePortrait(image: BufferedImage): Boolean {
        val width = image.width
        val height = image.height
        return (width.toDouble() / height.toDouble())*100.0 < 90.0
    }

    fun getTransparentDimension(image:BufferedImage): DimensionVo{
        val width = image.width
        val height = image.height

        var minWidth = 0
        var minHeight = 0
        var maxWidth = 0
        var maxHeight = 0

        for (y in 0 until height)
            for (x in 0 until width)
               if (image.getRGB(x, y) == 0 && minWidth == 0){
                   minWidth = x
                   minHeight = y
               }else if (image.getRGB(x, y) == 0 ){
                   if (x > maxWidth) maxWidth = x
                   if (y > maxHeight) maxHeight = y
               }

        return DimensionVo(minWidth,minHeight,maxWidth,maxHeight)

    }

    fun getTransparentDimensionOfMetadata(image: File): Array<DimensionVo>{
        val metadata = ImageMetadataReader.readMetadata(image)
        val directory = metadata.directories.find { it.name == "PNG-tEXt" }
        val comment = directory?.tags?.find { it.tagName == "Textual Data" }
        val jsonDimension = comment?.description?.replace("Comment: ","")

        if (jsonDimension != null){
            val dimensionType = object : TypeToken<Array<DimensionVo>>() {}.type
            return toObject<Array<DimensionVo>>(jsonDimension, dimensionType)
        }

        throw IllegalArgumentException("Imagem está sem o metadado")

    }

    private fun reviseOrientation(image: File): BufferedImage {
        val metadata = ImageMetadataReader.readMetadata(image)
        val directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory::class.java)
        val jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory::class.java)

        var orientation = 1
        try {
            orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION)

        } catch (me: MetadataException) {

        }

        val width = jpegDirectory.imageWidth
        val height = jpegDirectory.imageHeight
        println("width = $width height = $height orientation = $orientation")

        val information = ImageInformation(orientation, width, height)
        return transformImage(ImageIO.read(image), getExifTransformation(information))

    }

    private fun reviseOrientation(images: Array<File>): Array<BufferedImage> {
        val buffers = ArrayList<BufferedImage>(images.size)


        images.forEach { file -> buffers.add(reviseOrientation(file)) }
        return buffers.toTypedArray()
    }

    @Throws(Exception::class)
    fun transformImage(image: BufferedImage, transform: AffineTransform): BufferedImage {
        // call your function here
        val op = AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC)
        var destinationImage = op.createCompatibleDestImage(image,
                if (image.type == BufferedImage.TYPE_BYTE_GRAY) image.colorModel else null)
        val time = measureTimeMillis {
            destinationImage = op.filter(image, destinationImage)
        }
        println("time = ${time / 1000.0}")
        return destinationImage
    }


    private fun getExifTransformation(info: ImageInformation): AffineTransform {

        val t = AffineTransform()

        when (info.orientation) {
            1 -> {
            }
            2 // Flip X
            -> {
                t.scale(-1.0, 1.0)
                t.translate(-info.width.toDouble(), 0.0)
            }
            3 // PI rotation
            -> {
                t.translate(info.width.toDouble(), info.height.toDouble())
                t.rotate(Math.PI)
            }
            4 // Flip Y
            -> {
                t.scale(1.0, -1.0)
                t.translate(0.0, -info.height.toDouble())
            }
            5 // - PI/2 and Flip X
            -> {
                t.rotate(-Math.PI / 2)
                t.scale(-1.0, 1.0)
            }
            6 // -PI/2 and -width
            -> {
                t.translate(info.height.toDouble(), 0.0)
                t.rotate(Math.PI / 2)
            }
            7 // PI/2 and Flip
            -> {
                t.scale(-1.0, 1.0)
                t.translate(-info.height.toDouble(), 0.0)
                t.translate(0.0, info.width.toDouble())
                t.rotate(3 * Math.PI / 2)
            }
            8 // PI / 2
            -> {
                t.translate(0.0, info.width.toDouble())
                t.rotate(3 * Math.PI / 2)
            }
        }

        return t
    }


}

data class ImageInformation(val orientation : Int, val width: Int, val height: Int)