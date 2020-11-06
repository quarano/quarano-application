module.exports = {
  name: 'health-department-feature-export',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/health-department/feature-export',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
