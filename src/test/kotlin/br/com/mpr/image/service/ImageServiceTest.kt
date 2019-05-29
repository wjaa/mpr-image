package br.com.mpr.image.service

import org.junit.Test
import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.Image

class ImageServiceTest{

    @Test
    fun resize(){
        val imageService = ImageService()
        val image = File("/home/wagner/Downloads/teste.jpg")
        val imageDest = File("/home/wagner/Downloads/foto.jpg")

        imageService.resize(image,490,imageDest)
    }

    @Test
    fun merge(){
        val imageService = ImageService()
        val portaretrato = File("/home/wagner/Downloads/f1.png")
        val foto = File("/home/wagner/Downloads/foto.jpg")
        val imageDest = File("/home/wagner/Downloads/merge1.png")

        imageService.merge(foto,portaretrato,imageDest)

        val portaretrato2 = File("/home/wagner/Downloads/f2.png")
        val imageDest2 = File("/home/wagner/Downloads/merge2.png")

        imageService.merge(foto,portaretrato2,imageDest2)
    }
}
