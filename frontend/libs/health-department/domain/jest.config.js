module.exports = {
  name: 'health-department-domain',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/health-department/domain',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
