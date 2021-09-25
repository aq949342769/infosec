// 将信息转换为二进制
function toByte(ch) {
    // return 
}
// 将二进制转换为文本
function toText(b) {

}
// 流加密算法
function byteConvert(msg, key) {

    return msg.charCodeAt().toString(2)
}

var msg = document.querySelector("#msg")
var key = document.querySelector("#key")
var convert = document.querySelector("#convert")
var cipher = document.querySelector("#cipher")

convert.addEventListener("click", () => {
    msgText = msg.value
    keyText = key.value
    // var cipherText = byteConvert(msgText, keyText);
    var cipherText = byteConvert("中", "1111111");

    cipher.append(cipherText)
})