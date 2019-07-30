#MPR IMAGE
Responsável por redimencionar a imagens, corrigir orientação e fazer merge entre imagens.

###Features Principais  
- Adjustment Orientation
- Resize
- Merge

#####Adjustment Orientation
Esse método ajusta a orientação da imagem e faz o resize para o tamanho máximo enviado no em **maxSize**.

É necessário enviar o caminho da imagem resultante para tirar esse responsábilidade do método.

``
fun adjustAndResize(image:File, maxSize: Int, imageResult: File)
``
> Para entender melhor sobre image orientation recomendo ler esse site https://chunter.tistory.com/143

![Image Orientation Map](https://www.daveperrett.com/images/articles/2012-07-28-exif-orientation-handling-is-a-ghetto/EXIF_Orientations.jpg "Image Orientation Map")

#####Resize
Os métodos abaixo redimensionam a imagem mantendo a proporção e o tamanho máximo de um dos lados.

É necessário enviar o caminho da imagem resultante para tirar esse responsábilidade do método.

``
fun resizeByWidth(image: File, width: Int, imageResult : File)
``

``
fun resizeByHeight(image: File, height: Int, imageResult : File)
``

<img src="https://29comwzoq712ml5vj5gf479x-wpengine.netdna-ssl.com/wp-content/uploads/2013/04/michelle-obama.jpg" width="250">



#####Merge
Utilizado especificamente para encaixar imagens menores dentro de outra imagem maior.

Na prática a imagem maior tem algumas transparências, e fica na frente das imagens menores, gerando a imagem resultante.

``
fun merge(filesPictures: Array<File>, frameImage: File, imageResult: File)
``

O metodo acima depende de um atributo adicionado na imagem frame. Esse atributo é responsável por informar a localização
 dos pontos transparentes da imagem frame. No gimp esse metadata é chamado de Comments ou Comentário, no Exif ele é
 encontrado na estrutura abaixo:
 > - DirectoryName: PNG-tEXt
 > - TagName: Textual Data

> Saiba como adicionar um metadata no gimp em https://docs.gimp.org/2.10/en/gimp-introduction-whats-new-metadata.html

A estrutura dos pontos transparentes estão no formato JSON:

``
[
  {"startWidth":35,"startHeight":35,"endWidth":115,"endHeight":158},
  {"startWidth":144,"startHeight":37,"endWidth":267,"endHeight":118},
  {"startWidth":31,"startHeight":190,"endWidth":156,"endHeight":267},
  {"startWidth":185,"startHeight":149,"endWidth":266,"endHeight":268}
]
``


O metodo abaixo é mais inteligente e tenta encontrar um quadrado transparente, dentro da imagem front. Como é apenas uma
imagem a ser "encaixada", então é mais facil encontrar o espaço transparente.

``
fun merge (backImageFile: File, frontImageFile: File, imageResult: File)
``

O resultado é algo como abaixo:

<img src="https://www.pontofrio-imagens.com.br/decoracao/PortaRetrato/8935710/448929305/Porta-retrato-Kapos-Paramount-para-Foto-15x21-cm-Preto-8935710.jpg" width="250">

Encaixamos uma foto dentro de um porta-retrato.