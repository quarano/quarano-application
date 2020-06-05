module.exports = {
  name: 'client-contact-persons-contact-person-list',
  preset: '../../../../jest.config.js',
  coverageDirectory: '../../../../coverage/libs/client/contact-persons/contact-person-list',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
