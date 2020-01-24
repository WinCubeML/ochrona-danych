const entropy = (pass) => {
    const value = [];
    for (let i = 0; i < pass.length; i++) {
        value.push(pass.charCodeAt(i));
    }
    const unique = new Set(value);
    const log2 = Math.log2(65535);
    const result = unique.size * log2;
    const maxResult = pass.length * log2;
    return [result, maxResult];
};

const onPasswordChange = (pass) => {
    const password = pass.target.value;
    const [resultEntropy, maxEntropy] = entropy(password);
    const textEntropyPlaceholder = document.getElementById("passEntropy");
    textEntropyPlaceholder.innerText = "Entropia = " + resultEntropy + " / " + maxEntropy;
};

document.addEventListener('DOMContentLoaded', () => {
    const password = document.getElementById("pass1");
    password.addEventListener("input", onPasswordChange);
});