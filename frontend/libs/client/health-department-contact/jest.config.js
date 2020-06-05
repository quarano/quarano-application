module.exports = {
  name: 'client-health-department-contact',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/client/health-department-contact',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
