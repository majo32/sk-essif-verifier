var exec = require('child_process').exec;
var path = require('path');
const fs = require('fs')

const apis = [
    {
        path:"essif-verifier-server/essif-verifier-schemas/reference/essif-verifier-api.yaml",
        slug:"verifier",
        modulePrefix:"ESSIFVerifier",
        configurationPrefix:"ESSIFVerifier",
        serviceSuffix:"ESSIFService"
    }
];

function command(path, slug, modulePrefix, configurationPrefix, serviceSuffix){
    return "openapi-generator-cli generate "+
    "-i ../"+path+
    " -g typescript-angular "+
    "-o src/openapi/server/"+slug+" "+
    "--additional-properties=apiModulePrefix="+modulePrefix+",configurationPrefix="+configurationPrefix+",serviceSuffix="+serviceSuffix+",supportsES6=true,withInterfaces=true";
}

function rimraf(dir_path) {
    if (fs.existsSync(dir_path)) {
        fs.readdirSync(dir_path).forEach(function(entry) {
            var entry_path = path.join(dir_path, entry);
            if (fs.lstatSync(entry_path).isDirectory()) {
                rimraf(entry_path);
            } else {
                fs.unlinkSync(entry_path);
            }
        });
        fs.rmdirSync(dir_path);
    }
}


function oneStep(i){
    if(apis.length > i){
        const api = apis[i];
        var cmd = exec(command(api.path, api.slug, api.modulePrefix, api.configurationPrefix, api.serviceSuffix),
            function(err, stdout, stderr) {
            if (err) {
              console.log(err);
            }
            oneStep(i+1)
            console.log(stdout);
        });
    }
}

rimraf("src/openapi/server")
oneStep(0);

