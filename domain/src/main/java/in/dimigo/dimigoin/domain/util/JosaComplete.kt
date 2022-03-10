package `in`.dimigo.dimigoin.domain.util

fun String.josa(josa: String, onlyJosa: Boolean = false): String {

    val lastChar = if (isEmpty()) { 0 } else { this.last().code }
    val batchim = lastChar.minus(0xAC00).mod(28)

    return (if (onlyJosa) "" else this) + when (josa) {
        "을를", "를을" -> {
            if (batchim == None) "를" else "을"
        }
        "이가", "가이" -> {
            if (batchim == None) "가" else "이"
        }
        "으로" -> {
            if (batchim == None || batchim == Rieul) "로" else "으로"
        }
        else -> ""
    }
}

private const val None = 0 /* 받침 없음 */
private const val Rieul = 8 /* ㄹ */
