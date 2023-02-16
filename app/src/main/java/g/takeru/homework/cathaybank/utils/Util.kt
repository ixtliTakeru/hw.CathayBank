package g.takeru.homework.cathaybank.utils


fun String.toHttps(): String {
    return this.replace("http", "https")
}