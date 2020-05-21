module.exports = {
  name: 'health-department-ui-action-alert',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/health-department/ui-action-alert',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
