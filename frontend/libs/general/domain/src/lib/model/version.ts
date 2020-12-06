export interface VersionDto {
  git: {
    branch: string;
    commit: {
      id: string;
      time: Date;
    };
  };
  build: {
    artifact: string;
    name: string;
    time: Date;
    version: string;
    group: string;
  };
}
