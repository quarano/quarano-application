module.exports = {
  name: 'health-department-index-cases-case-list',
  preset: '../../../../jest.config.js',
  coverageDirectory:
    '../../../../coverage/libs/health-department/index-cases/case-list',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
