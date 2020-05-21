module.exports = {
  name: 'health-department-index-cases-action-list',
  preset: '../../../../jest.config.js',
  coverageDirectory:
    '../../../../coverage/libs/health-department/index-cases/action-list',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
