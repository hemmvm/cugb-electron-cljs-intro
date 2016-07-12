var electron = require('electron')
  , fs = require('fs-extra')


// /////////////////////////////////////////////////////////////////////////////
// Setup refs

var app = electron.app
  , BrowserWindow = electron.BrowserWindow
  , mainWindow = null


// /////////////////////////////////////////////////////////////////////////////
// Misc

fs.ensureDirSync(app.getPath('userData'))


// /////////////////////////////////////////////////////////////////////////////
// Ready

const browserWindowOptions = {
  height: 850,
  width: 1400,
  title: 'CUGB Electron Intro',
  icon: __dirname + '/img/logo_96x96.png'
};


app.on('ready', function() {
  mainWindow = new BrowserWindow(browserWindowOptions)

  mainWindow.setMenu(null)
  mainWindow.loadURL('file://' + __dirname + '/index.html')

  mainWindow.on('closed', function() {
    mainWindow = null
    app.quit()
  })
})
