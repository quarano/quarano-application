module.exports = {
  name: 'client-feature-contact-persons',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/client/feature-contact-persons',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
