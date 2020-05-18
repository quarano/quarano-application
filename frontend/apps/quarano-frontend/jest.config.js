module.exports = {
  name: 'quarano-frontend',
  preset: '../../jest.config.js',
  coverageDirectory: '../../coverage/apps/quarano-frontend',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
