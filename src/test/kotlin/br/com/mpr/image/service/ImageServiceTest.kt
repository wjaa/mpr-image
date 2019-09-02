package br.com.mpr.image.service

import org.junit.Assert
import org.junit.Test
import java.io.File
import javax.imageio.ImageIO

class ImageServiceTest{

    @Test
    fun resizeByWidth(){
        val imageService = ImageService()
        val fileImage = File(ImageServiceTest::class.java.classLoader.getResource("fotopaisagem.jpg").toURI())
        val fileImageDest = File.createTempFile("resizeByWidth",".tmp")
        imageService.resizeByWidth(fileImage,200,fileImageDest)
        Assert.assertTrue(fileImageDest.isFile)
        Assert.assertTrue(fileImageDest.exists())
        val image = ImageIO.read(fileImageDest)
        Assert.assertNotNull(image)
        Assert.assertEquals(200,image.width)

        fileImageDest.deleteOnExit()

    }

    @Test
    fun resizeByHeight(){
        val imageService = ImageService()
        val fileImage = File(ImageServiceTest::class.java.classLoader.getResource("fotopaisagem.jpg").toURI())
        val fileImageDest = File.createTempFile("resizeByHeight",".tmp")
        imageService.resizeByHeight(fileImage,200,fileImageDest)
        Assert.assertTrue(fileImageDest.isFile)
        Assert.assertTrue(fileImageDest.exists())
        val image = ImageIO.read(fileImageDest)
        Assert.assertNotNull(image)
        Assert.assertEquals(200,image.height)

        fileImageDest.deleteOnExit()

    }

    @Test
    fun merge(){
        val imageService = ImageService()

        val portaretrato = File(ImageServiceTest::class.java.classLoader.getResource("frame.png").toURI())
        val foto = File(ImageServiceTest::class.java.classLoader.getResource("fotopaisagem.jpg").toURI())
        val fileImageDest = File.createTempFile("merge",".tmp")
        imageService.merge(foto,portaretrato,fileImageDest)

        Assert.assertTrue(fileImageDest.isFile)
        Assert.assertTrue(fileImageDest.exists())
        val imageResult = ImageIO.read(fileImageDest)
        val imageFrame = ImageIO.read(portaretrato)
        Assert.assertNotNull(imageResult)
        //a imagem resultante tem que ter estar na horizontal.
        Assert.assertEquals(imageFrame.height,imageResult.width)
        Assert.assertEquals(imageFrame.width,imageResult.height)

        fileImageDest.deleteOnExit()

    }

    @Test
    fun mergePortrait(){
        val imageService = ImageService()
        val portaretrato = File(ImageServiceTest::class.java.classLoader.getResource("frame2.png").toURI())
        val foto = File(ImageServiceTest::class.java.classLoader.getResource("fotoretrato.jpg").toURI())
        val fileImageDest = File.createTempFile("mergePortrait",".tmp")
        imageService.merge(foto,portaretrato,fileImageDest)
        Assert.assertTrue(fileImageDest.isFile)
        Assert.assertTrue(fileImageDest.exists())
        val imageResult = ImageIO.read(fileImageDest)
        val imageFrame = ImageIO.read(portaretrato)
        Assert.assertNotNull(imageResult)
        Assert.assertEquals(imageFrame.width,imageResult.width)
        Assert.assertEquals(imageFrame.height,imageResult.height)
        fileImageDest.deleteOnExit()

    }



    @Test
    fun mergeFrameMultiPic(){
        val imageService = ImageService()
        var images = ArrayList<File>(16)
        for ( i in 1..16 )
            images.add(
                    if (i % 2 == 0) File(ImageServiceTest::class.java.classLoader.getResource("fotoretrato.jpg").toURI())
                    else File(ImageServiceTest::class.java.classLoader.getResource("fotopaisagem.jpg").toURI())
            )

        var portaretrato = File(ImageServiceTest::class.java.classLoader.getResource("quadro.png").toURI())
        val fileImageDest = File.createTempFile("mergeFrameMultiPic",".tmp")
        imageService.merge(images.toTypedArray(),portaretrato,fileImageDest)
        Assert.assertTrue(fileImageDest.isFile)
        Assert.assertTrue(fileImageDest.exists())
        val imageResult = ImageIO.read(fileImageDest)
        val imageFrame = ImageIO.read(portaretrato)
        Assert.assertNotNull(imageResult)
        Assert.assertEquals(imageFrame.width,imageResult.width)
        Assert.assertEquals(imageFrame.height,imageResult.height)
        fileImageDest.deleteOnExit()

    }


    @Test
    fun getTransparentDimension(){
        val imageService = ImageService()
        val portaretrato = File(ImageServiceTest::class.java.classLoader.getResource("frame.png").toURI())

        val dimension = imageService.getTransparentDimension(ImageIO.read(portaretrato))
        Assert.assertNotNull(dimension)
        Assert.assertTrue(dimension.startWidth > 0)
        Assert.assertTrue(dimension.startHeight > 0)
        Assert.assertTrue(dimension.endHeight > 0)
        Assert.assertTrue(dimension.endWidth > 0)
    }

    @Test
    fun getTransparentDimensionOfMetadata(){
        val imageService = ImageService()
        val portaretrato = File(ImageServiceTest::class.java.classLoader.getResource("quadro.png").toURI())

        val dimensions = imageService.getTransparentDimensionOfMetadata(portaretrato)
        Assert.assertNotNull(dimensions)
        dimensions.forEach { dimension ->
            Assert.assertTrue(dimension.startWidth > 0)
            Assert.assertTrue(dimension.startHeight > 0)
            Assert.assertTrue(dimension.endHeight > 0)
            Assert.assertTrue(dimension.endWidth > 0)
        }
    }

    @Test
    fun getTransparentDimensionOfMetadata2(){
        val imageService = ImageService()
        val portaretrato = File(ImageServiceTest::class.java.classLoader.getResource("frame3.png").toURI())

        val dimensions = imageService.getTransparentDimensionOfMetadata(portaretrato)
        Assert.assertNotNull(dimensions)
        dimensions.forEach { dimension ->
            Assert.assertTrue(dimension.startWidth > 0)
            Assert.assertTrue(dimension.startHeight > 0)
            Assert.assertTrue(dimension.endHeight > 0)
            Assert.assertTrue(dimension.endWidth > 0)
        }
    }

    @Test
    fun getTransparentDimensionOfMetadata3(){
        val imageService = ImageService()
        val portaretrato = File(ImageServiceTest::class.java.classLoader.getResource("quadro2.png").toURI())

        val dimensions = imageService.getTransparentDimensionOfMetadata(portaretrato)
        Assert.assertNotNull(dimensions)
        dimensions.forEach { dimension ->
            Assert.assertTrue(dimension.startWidth > 0)
            Assert.assertTrue(dimension.startHeight > 0)
            Assert.assertTrue(dimension.endHeight > 0)
            Assert.assertTrue(dimension.endWidth > 0)
        }
    }

    @Test
    fun adjustAndResize(){
        val imageService = ImageService()
        val foto = File(ImageServiceTest::class.java.classLoader.getResource("fotoretrato.jpg").toURI())
        val fileImageDest = File.createTempFile("adjustAndResize",".tmp")
        imageService.adjustAndResize(foto,200,fileImageDest)

        imageService.resizeByWidth(fileImageDest,200,fileImageDest)
        Assert.assertTrue(fileImageDest.isFile)
        Assert.assertTrue(fileImageDest.exists())
        val image = ImageIO.read(fileImageDest)
        Assert.assertNotNull(image)
        Assert.assertEquals(200,image.width)

        fileImageDest.deleteOnExit()

    }

}
