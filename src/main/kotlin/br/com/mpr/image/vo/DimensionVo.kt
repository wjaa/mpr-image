package br.com.mpr.image.vo

data class DimensionVo(val minWidth:Int, val minHeight: Int, val maxWidth: Int, val maxHeight: Int){

    fun isPortrait(): Boolean{
        return maxHeight - minHeight > maxWidth - minWidth
    }

}
