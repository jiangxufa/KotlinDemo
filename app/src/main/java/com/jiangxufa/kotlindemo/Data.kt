package com.jiangxufa.kotlindemo

/**
 * 创建时间：2018/9/10
 * 编写人：lenovo
 * 功能描述：
 */
data class Spu(var id: Int, var name: String, var spuItems: List<SpuItem>, var number: Int = 0)

data class SpuItem(var id: Int, var spuId: Int, var name: String, var select: Boolean = false)

object Data {
    fun createSpuData(): List<Spu> {
        val list = mutableListOf<Spu>()
        (0..20).forEach {
            list.add(Spu(it, "SPU $it", createSpuItemData(it)))
        }
        return list
    }

    fun createSpuItemData(spuId: Int): ArrayList<SpuItem> {
        val list = ArrayList<SpuItem>()
        (0..20).forEach {
            list.add(SpuItem(it, spuId, "SPU $spuId SpuItem $it"))
        }
        return list
    }
}