package br.com.mpr.image.service

import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.Image
import java.awt.image.AffineTransformOp
import java.awt.geom.AffineTransform




class ImageService{

    fun resize(image: File, width: Int, imageResult : File){
        val sourceImage = ImageIO.read(image)
        val thumbnail = sourceImage.getScaledInstance(width, -1, Image.SCALE_SMOOTH)
        val bufferedThumbnail = BufferedImage(thumbnail.getWidth(null),
                thumbnail.getHeight(null),
                BufferedImage.TYPE_INT_RGB)
        bufferedThumbnail.graphics.drawImage(thumbnail, 0, 0, null)
        ImageIO.write(bufferedThumbnail, "jpg", imageResult)

    }

    fun merge (backImageFile: File, frontImageFile: File, imageResult: File ){
        val backImage = ImageIO.read(backImageFile)
        var frontImage = ImageIO.read(frontImageFile)

        if(!imagePortrait(backImage)){
            frontImage = rotate(frontImage)
        }

        // create the new image, canvas size is the max. of both image sizes
        //val w = Math.max(backImage.width, frontImage.width)
        //val h = Math.max(backImage.height, frontImage.height)
        val w = frontImage.width
        val h = frontImage.height
        val combined = BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)

        // paint both images, preserving the alpha channels
        val g = combined.graphics

        //adicionando uma margem na foto do cliente
        g.drawImage(backImage, 50, 50, null)
        g.drawImage(frontImage, 0, 0, null)

        // Save as new image
        ImageIO.write(combined, "PNG", imageResult)
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

    private fun imagePortrait(frontImage: BufferedImage?): Boolean {
        return false
    }
}

