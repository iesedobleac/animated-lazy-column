package com.isaacdelosreyes.lazycolumnanimation

sealed class ItemSealedClass {

    data class Header(val title: String): ItemSealedClass()
    data class Item(val title: String): ItemSealedClass()
}
