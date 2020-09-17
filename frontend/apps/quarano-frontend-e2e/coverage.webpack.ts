module.exports = {
  module: {
    rules: [
      {
        test: /\.(ts)$/,
        loader: 'istanbul-instrumenter-loader',
        options: {esModules: true},
        enforce: 'post',
        include: [
          require('path').join(__dirname, '..', 'quarano-frontend', 'src'),
          require('path').join(__dirname, '..', '..', 'libs')
        ],
        exclude: [
          /\.(e2e|spec)\.ts$/,
          /node_modules/,
          /main.ts/,
          /(ngfactory|ngstyle)\.js/
        ]
      }
    ]
  }
};
