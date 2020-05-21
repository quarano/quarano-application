module.exports = {
  name: 'health-department-index-cases-shell',
  preset: '../../../../jest.config.js',
  coverageDirectory:
    '../../../../coverage/libs/health-department/index-cases/shell',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
