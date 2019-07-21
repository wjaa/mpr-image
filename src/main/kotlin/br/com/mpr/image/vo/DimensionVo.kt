package br.com.mpr.image.vo

data class DimensionVo(val startWidth:Int, val startHeight: Int, val endWidth: Int, val endHeight: Int){

    fun isPortrait(): Boolean{
        return endHeight - startHeight > endWidth - startWidth
    }

}
