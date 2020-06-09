module.exports = {
  name: 'health-department-feature-contact-cases',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/health-department/feature-contact-cases',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
