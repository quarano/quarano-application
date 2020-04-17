/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 2.20.583 on 2020-04-09 11:03:55.

export interface ContactPerson {
    id: number;
    client: Client;
    surename: string;
    firstname: string;
    phone: string;
    email: string;
    typeOfContract: TypeOfContract;
    typeOfProtection: TypeOfProtection;
}

export interface Client {
    clientId: number;
    clientCode: string;
    surename: string;
    firstname: string;
    phone: string;
    street: string;
    zipCode: string;
    completedPersonalData: boolean;
    completedQuestionnaire: boolean;
    completedContactRetro: boolean;
    quarantineStartDateTime: Timestamp;
    quarantineEndDateTime: Timestamp;
    infected: boolean;
    type: ClientType;
    healthDepartment: HealthDepartment;
    comments: FirstReport[];
}

export interface Timestamp extends Date {
}

export interface HealthDepartment {
    id: string;
    fullName: string;
    passCode: string;
}

export interface FirstReport {
    id: number;
    dateTime: Date;
    min15MinutesContactWithC19Pat: boolean;
    nursingActionOnC19Pat: boolean;
    directContactWithLiquidsOfC19pat: boolean;
    flightPassengerCloseRowC19Pat: boolean;
    flightCrewMemberWithC19Pat: boolean;
    belongToMedicalStaff: boolean;
    belongToNursingStaff: boolean;
    belongToLaboratoryStaff: boolean;
    familyMember: boolean;
    otherContactType: boolean;
    passengerOnSameFlightAsPatient: boolean;
}

export type TypeOfContract = "O" | "S" | "P" | "AE" | "Aer" | "Mat" | "And";

export type TypeOfProtection = "Zero" | "M1" | "M2" | "K" | "H" | "S";

export type ClientType = "INDEX_CASE" | "CONTACT_CASE";
