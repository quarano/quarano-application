export interface FirstQueryQuestion {
  question: string;
  attributeName: string;
  answerType: string;
}

export const FIRST_QUERY_QUESTIONS = {
  0: {
    question: 'Hatten Sie bereits Kontakt mit einer Person, bei der COVID-19 nachgewiesen wurde?',
    answerType: 'bool',
    attributeName: 'Min15MinutesContactWithC19Pat'
  },
  1: {
    question: 'Haben Sie eine Person gepflegt / versorgt, bei der COVID-19 festgestellt wurde?',
    answerType: 'bool',
    attributeName: 'NursingActionOnC19Pat'
  },
  2: {
    question: 'Hatten Sie direkten Kontakt zu Körperflüssigkeiten von einer Person, bei der COVID-19 festgestellt wurde?',
    answerType: 'bool',
    attributeName: 'DirectContactWithLiquidsOfC19Pat'
  },
  3: {
    question: 'Saßen Sie auf einem FLug nahe an einer Person, bei der COVID-19 festgestellt wurde?',
    answerType: 'bool',
    attributeName: 'FlightPassengerWithCloseRowC19Pat'
  },
  4: {
    question: 'Waren Sie Crewmitglied auf einem FLug mit einer Person an Board, bei der COVID-19 festgestellt wurde?',
    answerType: 'bool',
    attributeName: 'FlightAsCrewMemberWithC19Pat'
  },
  5: {
    question: 'Sind Sie in einem medizinischen Beruf tätig?',
    answerType: 'bool',
    attributeName: 'BelongToMedicalStaff'
  },
  6: {
    question: 'Sind Sie in einem Pflegeberuf tätig?',
    answerType: 'bool',
    attributeName: 'BelongToNursingStaff'
  },
  7: {
    question: 'Sind Sie in einem Labor tätig?',
    answerType: 'bool',
    attributeName: 'BelongToLaboratoryStaff'
  },
  8: {
    question: 'Wurde bei einem Ihrer Familienmitglieder COVID-19 festgestellt?',
    answerType: 'bool',
    attributeName: 'FamilyMember'
  },
  9: {
    question: 'Hatten Sie auf andere Art Kontakt mit einer Person, bei der COVID-19 festgestellt wurde?',
    answerType: 'text',
    attributeName: 'OtherContactType'
  }
};
