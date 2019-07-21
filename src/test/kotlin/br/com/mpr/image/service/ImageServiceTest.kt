package br.com.mpr.image.service

import org.junit.Test
import java.io.File
import javax.imageio.ImageIO
import kotlin.collections.ArrayList

class ImageServiceTest{

    @Test
    fun resize(){
        val imageService = ImageService()
        val image = File("/home/wagner/Downloads/teste.jpg")
        val imageDest = File("/home/wagner/Downloads/foto.jpg")

        imageService.resizeByWidth(image,490,imageDest)
    }

    @Test
    fun mergeVariasImagens(){
        val imageService = ImageService()
        for (x in 1 until 6)
            for(y in 1 until 12){
                val portaretrato = File("/home/wagner/Downloads/teste/pr/pr$x.png")
                val foto = File("/home/wagner/Downloads/teste/foto/f$y.jpg")
                val imageDest = File("/home/wagner/Downloads/teste/preview/f${y}pr$x.png")
                imageService.merge(foto,portaretrato,imageDest)
            }

    }

    @Test
    fun mergePortrait(){
        val imageService = ImageService()
        val portaretrato = File("/home/wagner/Downloads/teste/pr/pr1.png")
        val foto = File("/home/wagner/Downloads/teste/foto/f7.jpg")
        val imageDest = File("/home/wagner/Downloads/teste/preview/f7pr1.png")
        imageService.merge(foto,portaretrato,imageDest)


    }



    @Test
    fun mergeFrame(){
        val imageService = ImageService()
        var images = ArrayList<File>(16)
        for ( i in 1..16 ) images.add(File("/home/wagner/Downloads/teste/foto/f$i.jpg"))
        var quadro = File("/home/wagner/Downloads/teste/pr/quadro.png")
        val imageDest = File("/home/wagner/Downloads/teste/preview/quadroPreview.png")
        imageService.merge(images.toTypedArray(),quadro,imageDest)


    }


    @Test
    fun getTransparentDimension(){
        val imageService = ImageService()
        val portaretrato = File("/home/wagner/Downloads/pr.png")

        println(imageService.getTransparentDimension(ImageIO.read(portaretrato)))
    }


    @Test
    fun getMetadata(){
        val imageService = ImageService()
        val portaretrato = File("/home/wagner/Downloads/quadro.png")

        var dimensions = imageService.getTransparentDimensionOfMetadata(portaretrato)

        dimensions.forEach { d -> println(d)}
    }

    @Test
    fun adjustAndResize(){
        val imageService = ImageService()

        for ( i in 1..16 ) imageService.adjustAndResize(File("/home/wagner/Downloads/teste/foto_original/f$i.jpg"),
                600,File("/home/wagner/Downloads/teste/foto/f$i.jpg"))

    }
}
