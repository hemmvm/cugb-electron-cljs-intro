module.exports = function(grunt) {
'use strict';

var moment = require('moment'),
      path = require('path'),
  packager = require('electron-packager');

var os = (function(){
  var platform = process.platform;
  if (/^win/.test(platform))    { return "windows"; }
  if (/^darwin/.test(platform)) { return "mac"; }
  if (/^linux/.test(platform))  { return "linux"; }
  return null;
})();

var exe = {
  windows:  "electron.exe",
  mac:  "Electron.app/Contents/MacOS/Electron",
  linux:  "electron"
};

var electron_path = "electron";
// var electron_version = "1.0.2";
var electron_version = "1.1.1";

var packageJson = require(__dirname + '/package.json');

//------------------------------------------------------------------------------
// ShellJS
//------------------------------------------------------------------------------

require('shelljs/global');
// shelljs/global makes the following imports:
//   cwd, pwd, ls, find, cp, rm, mv, mkdir, test, cat,
//   str.to, str.toEnd, sed, grep, which, echo,
//   pushd, popd, dirs, ln, exit, env, exec, chmod,
//   tempdir, error

var shellconfig = require('shelljs').config;
shellconfig.silent = false; // hide shell cmd output?
shellconfig.fatal = false;   // stop if cmd failed?

//------------------------------------------------------------------------------
// Grunt Config
//------------------------------------------------------------------------------


grunt.initConfig({

  'download-electron': {
    version: electron_version,
    outputDir: 'electron'
  }

});

//------------------------------------------------------------------------------
// Third-party tasks
//------------------------------------------------------------------------------


grunt.loadNpmTasks('grunt-download-electron');
if (os === "mac") {
  grunt.loadNpmTasks('grunt-appdmg');
}
grunt.loadNpmTasks('winresourcer');

//------------------------------------------------------------------------------
// Setup Tasks
//------------------------------------------------------------------------------

grunt.registerTask('setup', [
  'download-electron'
]);


grunt.registerTask('launch', function(async) {
  var IsAsync = (async == "true");
  grunt.log.writeln("\nLaunching development version...");
  var local_exe = exe[os];
  exec(path.join(electron_path, local_exe) + " app", {async:IsAsync});
});

//------------------------------------------------------------------------------
// Release Helper functions
//------------------------------------------------------------------------------

function setReleaseConfig(build, paths) {
  grunt.log.writeln("\nRemoving config to force default release settings...");
  rm('-f', paths.releaseCfg);
  cp(paths.prodCfg, paths.releaseCfg);
}

function getBuildMeta() {
  grunt.log.writeln("Getting project metadata...");
  var tokens = cat("project.clj").split(" ");
  var build = {
    name:    tokens[1],
    version: tokens[2].replace(/"/g, "").trim(),
    date:    moment().format("YYYY-MM-DD")
  };
  var commit = exec("git rev-list HEAD --count", {silent:true}).output.trim();
  if (commit != '') {
    build.commit = "pre";
  } else {
    build.commit = commit;
  }
  build.releaseName = build.name + "-v" + build.version + "-" + build.commit;
  return build;
}

function getReleasePaths(build) {
  var paths = {
    builds: "builds",
    devApp: "app",
    rootPkg: "package.json"
  };
  paths.release = path.join(paths.builds, build.releaseName);
  paths.devPkg = path.join(paths.devApp, "package.json");
  paths.prodCfg = path.join(paths.devApp, "prod.config.json");
  paths.releaseApp = path.join(paths.builds, paths.devApp);
  paths.releasePkg = path.join(paths.releaseApp, "package.json");
  paths.releaseCfg = path.join(paths.releaseApp, "config.json");
  paths.releaseResources = path.join(paths.releaseApp, "components");
  return paths;
}

function getBasicReleaseInfo(build, paths) {
  var opts = {
    "dir": paths.releaseApp,
    "name": packageJson.name,
    "version": electron_version,
    "asar": true,
    "out": paths.release,
    "overwrite": true,
    "app-bundle-id": "com.example",
    "app-version": build.version,
    "version-string": {
      "ProductVersion": build.version,
      "ProductName": packageJson.name,
    }
  };
  return opts;
}

function stampRelease(build, paths) {
  grunt.log.writeln("\nStamping release with build metadata...");
  var pkg = grunt.file.readJSON(paths.releasePkg);
  pkg.version = build.version;
  pkg["build-commit"] = build.commit;
  pkg["build-date"] = build.date;
  JSON.stringify(pkg, null, "  ").to(paths.releasePkg);
}

function defineRelease(done, extra_opts, cb) {
  var callback = cb || (function () {});
  var build = getBuildMeta();
  var paths = getReleasePaths(build);
  var basic_opts = getBasicReleaseInfo(build, paths);
  var opts = Object.assign(basic_opts, extra_opts);

  packager(opts, function(err, appPath) {
    if (err) {
      grunt.log.writeln("Error: ".red, err);
    }
    if (appPath) {
      if (Array.isArray(appPath)) {
        appPath.forEach(function(i) {
          callback(i);
          grunt.log.writeln("Build: " + i.cyan);
        });
      } else {
        callback(appPath);
        grunt.log.writeln("Build: " + appPath.cyan);
      }
    }
    done(err);
  });
}

function deleteExtraResources(paths) {
  rm('-rf', path.join(paths.releaseApp, "js", "p", "out"));
}


//------------------------------------------------------------------------------
// Tasks
//------------------------------------------------------------------------------

grunt.registerTask('release', ['cljsbuild-prod', 'prepare-release', 'release-linux', 'release-win']);


grunt.registerTask('cljsbuild-prod', function() {
  grunt.log.writeln("\nCleaning and building ClojureScript production files...");
  exec("lein do clean, with-profile production cljsbuild once");
});


grunt.registerTask('prepare-release', function() {
  var build = getBuildMeta();
  var paths = getReleasePaths(build);

  grunt.log.writeln("name:    "+build.name.cyan);
  grunt.log.writeln("version: "+build.version.cyan);
  grunt.log.writeln("date:    "+build.date.cyan);
  grunt.log.writeln("commit:  "+build.commit.cyan);
  grunt.log.writeln("release: "+build.releaseName.cyan);

  mkdir('-p', paths.builds);

  if (test("-d", paths.releaseApp)) {
    rm('-r', paths.releaseApp);
  }

  if (test("-d", paths.release)) {
    rm('-rf', paths.release);
  }

  //copy app folder
  cp('-r', paths.devApp, paths.builds);

  grunt.log.writeln("\nCopying node dependencies to release...");
  cp('-f', paths.rootPkg, paths.releaseApp);
  pushd(paths.releaseApp);
  exec('npm install --no-optional --production --silent');
  popd();
  cp('-f', paths.devPkg, paths.releaseApp);

  deleteExtraResources(paths);
  stampRelease(build, paths);
  setReleaseConfig(build, paths);
});


grunt.registerTask('release-linux', function() {
  var done = this.async();
  var opts = {
    "arch": ["ia32", "x64"],
    "platform": "linux"
  }
  defineRelease(done, opts);
});



grunt.registerTask('release-win', function() {
  var done = this.async();
  var build = getBuildMeta();
  var cb = function (appPath) {
    grunt.log.writeln("\nSkipping windows installer creation:", "makensis not installed or not in path".cyan);
  };

  var opts = {
    "arch": ["ia32", "x64"],
    "platform": "win32",
    "icon": "app/img/logo.ico"
  }
  defineRelease(done, opts, cb);
});


//------------------------------------------------------------------------------
// Default Task
//------------------------------------------------------------------------------

grunt.registerTask('default', ['setup']);

// end module.exports
};
