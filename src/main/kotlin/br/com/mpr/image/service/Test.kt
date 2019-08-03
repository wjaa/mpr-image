package br.com.mpr.image.service

import kotlin.math.abs

fun diagonalDifference(arr: Array<Array<Int>>): Int {
    var d1 = 0
    var d2 = 0
    arr.forEachIndexed { y, ints ->  ints.forEachIndexed { x, i ->  if(y ==x) d1+=i }}
    arr.forEachIndexed { y, ints ->  ints.forEachIndexed { x, i ->  if(y+x == ints.size-1) d2+=i }}
    return abs(d1 - d2)
}