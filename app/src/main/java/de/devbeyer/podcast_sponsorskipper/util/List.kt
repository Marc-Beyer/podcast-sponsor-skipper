package de.devbeyer.podcast_sponsorskipper.util

fun <T> List<T>.toTripleList(): List<Triple<T?, T?, T?>> {
    val result = mutableListOf<Triple<T?, T?, T?>>()
    val iterator = this.iterator()

    while (iterator.hasNext()) {
        val first = if (iterator.hasNext()) iterator.next() else null
        val second = if (iterator.hasNext()) iterator.next() else null
        val third = if (iterator.hasNext()) iterator.next() else null
        result.add(Triple(first, second, third))
    }

    return result
}