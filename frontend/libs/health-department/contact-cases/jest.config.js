module.exports = {
  name: 'health-department-contact-cases',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/health-department/contact-cases',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
