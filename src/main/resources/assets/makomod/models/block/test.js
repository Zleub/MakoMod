fs = require('fs')

file = fs.readFileSync(process.argv[2]).toString()
  .replace(/^v (-?\d*) (-?\d*) (-?\d*)$/gm, (m, a, b, c) => `v ${a / 16 + 0.5} ${b / 16 + 0.5} ${c / 16 + 0.5}`)

// console.log(file)
fs.renameSync(process.argv[2], process.argv[2] + ".old")
fs.writeFileSync(process.argv[2], file)
