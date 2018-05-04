let fs = require("fs");
let prepare = require("./prepare.js");

let argv = process.argv;
let start_file = argv[2];
let in_dir = argv[3];
let out_dir = argv[4];

prepare.build_skeleton(start_file, null, (dict)=>{
    console.log(dict[0].districts['北京市'].name);
});
// prepare.run(skeleton, in_dir, out_dir);