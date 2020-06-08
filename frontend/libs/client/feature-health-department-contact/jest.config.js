module.exports = {
  name: 'client-feature-health-department-contact',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/client/feature-health-department-contact',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
