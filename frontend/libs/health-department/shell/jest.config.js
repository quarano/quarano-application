module.exports = {
  name: 'health-department-shell',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/health-department/shell',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
