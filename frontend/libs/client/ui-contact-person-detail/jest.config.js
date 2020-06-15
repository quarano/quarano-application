module.exports = {
  name: 'client-ui-contact-person-detail',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/client/ui-contact-person-detail',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
