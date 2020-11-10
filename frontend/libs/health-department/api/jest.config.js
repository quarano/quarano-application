module.exports = {
  name: 'health-department-api',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/health-department/api',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
