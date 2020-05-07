## Wolf Hospital Management Database System
# For Hospital in North Carolina

#Goal
To create a robust database system able to used within the Command Line (User Interface) that can be operated by a Nurse, Doctor and/or Patient.

# Assumptions
1. Assume that the nurse does not view/update medical records. They simply take care of daily routine/maintenance of patients in the ward.
2. Assume a ward has exactly one responsible nurse but a nurse can be responsible for many wards.
3. Assume a hospital staff member can work for multiple departments.
4. Assume a patient can be assigned to at most one ward.
5. Assume a staff has one job title and one professional title
6. Assume medical record cannot be created without a responsible doctor and has exactly one responsible doctor for creating and updating medical records.
7. Assume medical records are composed of many tests, prescriptions and diagnosis details.
8. Assume a doctor can recommend many tests.
9. Assume a specialist can perform many tests.
10. Assume that many registration operators can assist many patients on a variety of tasks. A patient may have their check-in information processed by multiple operators (one for check-in one for check-out). Same for ward assignment if a patient is reassigned to a different ward.
11. Assume tests and treatments are associated with fees.
12. Assume fees compose the billing record.
13. Assume a billing account has exactly one billing record for which a patient is billed for.
14. Assume it is not necessary for a registration operator to view the medical records, they bill the patient based on the billing account and billing record.
15. Assume that a prescription is a treatment for a patient.
16. Assume that medical records are created for each patient for each visit similar to billing accounts.
17. Assume a treatment in the hospital will be defined as a prescription given to the patient.
18. Assume a test is not added to a patient’s medical record until the test has been completed.

#1. Problem Description
The Wolf Hospital Database Management System for a hospital in North Carolina will be used by management, which includes registrations operators, nurses, and doctors. Each user will have specific tasks and operations that can be performed using this system. The database system will maintain information about staff, patients, check-in, billing, medical records, and ward.
The purpose of this database system is to provide an interface for information storage and retrieval to allow the staff to perform these tasks and operations more efficiently. These tasks include:
  i. Process information about staff, patients, and wards
  ii. Check, assign, and reserve wards or beds for patients
  iii. Provide a new medical record for each treatment, test, and check-in per patient
  iv. Generate billing accounts for each patient
  v. Report medical history
The advantages of this database when implemented correctly. The staff will now be able to maintain billing accounts and medical records for each person, process information and generate reports efficiently.
#2. User Classes
The Project Narrative lists four tasks that are intended for three classes of users that the database system will need to support. These user classes are:
Registration Operators are users who process information for patients that are admitted into the hospital for check-in. Operators are responsible for generating reports and checking the availability of ward information, assigning and reserving a bed for each patient. Operators can also bill patients and their insurance, as well a track billing information. Operators can also view their own personal information and also information of the doctors and nurses.
Doctors recommend and perform tests for patients and manage medical records. This user inputs the data for the treatment, test, prescription, and diagnosis.
Nurses check available wards/beds and assign patients. Nurses also have to ability to treat patients.
#3. Main Entities
The five entities that are the most relevant to the tasks and operations required by our clients are the following.
● Patient/Staff: ID, name, age, gender, address, phone#
o Patient: SSN (if available), DOB (date of birth), status (processing
treatment plan, in ward, completing treatment)
o Staff: job title (doctor, nurse, operator), professional title, department
● Ward information: ward number, capacity, patient’s SSN, charges per day
● Check-in/out information: patient ID, ward number, bed number, start date, end
date.
● Patient medical history information: patient ID, start date, end date, responsible
doctor, prescription, diagnosis details, tests
● Billing information: patient ID, responsibleSSN, billing address, payment
information, billing records (total), visit date
#4. Database Usage in a Realistic Situation 4.1. First-time patient check-in
A patient has entered the hospital and states that it is their first time visiting this particular hospital. The registration operator begins to enter the basic information of the patient and check ward availability to assign the patient to the bed.

**4.2. New hire staff**
The hospital is looking for a new nurse because they realized a new hire could help them treat more patients efficiently. After the nurse is hired for the position, the registration operator updates staff information in the database system.

**4.3. Check out patient**
The doctor comes into a patient room and has decided the patient is finished with treatment and is clear for discharge from the hospital. The doctor updates the patient medical records with the recent treatment and diagnosis details. The registration operator then releases the bed of the patient and generates a bill for the patient for their treatment and stay.

#5. Application Program Interfaces

**5.1. Information Processing**
● newPatient(ID, name, age, gender, address, phone#, DOB, SSN, status) ▪ Return ID or NULL for error
● newStaff(ID, name, age, gender, address, phone#, jobTitle, department, professionalTitle)
▪ Return ID or NULL for error
● addWard(wardNumber, capacity, patientsSSN, chargesPerDay)
▪ Return number or NULL for error
● updatePatientInfo(ID, name, age, gender, address, phone#, DOB, SSN, status)
▪ Return confirmation
▪ if NULL value for name/age/gender/address/phone#/DOB/SSN/status, these will not be updated
● updateStaffInfo(ID, name, age, gender, address, phone#, jobTitle, department, professionalTitle)
▪ Return confirmation
▪ if NULL value for name/age/gender/address/phone#/jobTitle/professionalTitle, these will not be updated
● updateWardInfo(wardNumber, capacity, patientsSSN, chargesPerDay)
▪ Return confirmation
▪ if NULL value for name/age/gender/address/phone#/jobTitle/professionalTitle, these will not be updated
● deletePatient(ID)
▪ Return confirmation
● deleteStaff(ID)
▪ Return confirmation
● deleteWard(wardNumber)
▪ Return confirmation
● checkWardAvailability(wardNumber)
▪ Return the number of available beds
● assignBedToPatient(patientID, wardNumber, bedNumber) ▪ Return confirmation
● reserveBed(wardNumber, bedNumber) ▪ Return confirmation
● releaseBed(wardNumber, bedNumber) ▪ Return confirmation

**5.2. Maintaining Medical Records**
● createMedicalRecord(patientID, startDate, endDate) ▪ Return confirmation
● updateMedicalRecord(patientID, startDate, endDate)
▪ Return confirmation
▪ if NULL value for startDate/endDate, these will not be updated
● addTestToMedicalRecord(patientID, testName, result)
▪ Return confirmation
● addTreatmentToRecord(patientID, prescriptionName)
▪ Return confirmation
● createCheckInInfo(patientID, wardNumber, bedNumber, startDate, endDate)
▪ Return confirmation
● updateCheckInInfo(patientID, wardNumber, bedNumber, startDate, endDate)
▪ Return confirmation
▪ if NULL value for wardNumber/bedNumber/startDate/endDate, these
will not be updated

**5.3. Maintaining Billing Accounts**
● spaceAvailable()
▪ Return Boolean indicating space available for patient
● createBillingAccount(patientID, responsibleSSN, billingAddress, visitDate, paymentInfo, billingRecordID)
▪ Return confirmation
● updateBillingAccount(patientID, responsibleSSN, billingAddress, visitDate, paymantInfo, billingRecordID)
▪ Return confirmation
▪ if NULL value for responsibleSSN/billingAddress/visitDate/paymentInfo, these will not be updated
● addFeeToBillingRecord(billingRecordID, feeID) ▪ Return confirmation
5.4. Reports
● getPatientMedicalRecord(patientID, startDate, endDate)
▪ Return medical record for given patient within date range
● totalOccupiedBed()
▪ Return the total number of occupied beds
● totalAvailableBed()
▪ Return the total number of available beds
● totalOccupiedWard()
▪ Return the total number of occupied wards
● totalAvailableWard()
▪ Return the total number of available wards
● wardUsageRate()
▪ Return the percentage of ward usage
● totalPatientPerMonth(month)
▪ Return the total number of patient for this month
● patientListByDoctor(doctorID)
▪ Return list of patient treated by a given doctor
● listStaffByRole(jobTitle)
▪ Return a list of staff with given job title

#6. Data Views

**6.1. Registration Operator**
Registration operators have several responsibilities such as processing check-in information, registering first-time patients, billing patients for their treatments and stay and assigning patients to a ward. Since operators have the most responsibility in the system, it is reasonable to assume they will have the most privilege. The following are data elements that the registration operator should be able to view in order to complete their tasks:
● The table of patient information in order to register first-time patients and update patient information.
● The table of ward information in order to check availability and assign patients to a ward.
● The table of staff information in order to update or create new entries.
● The table with information on patients’ billing accounts and the associated billing
record in order to bill patients for their treatment and stay.
We don’t find it necessary for the registration operator to view patients’ medical records as all fees necessary for billing the patient are present in the billing record.

**6.2. Doctor**
Doctors are responsible for treating patients, recommending and performing tests, and maintaining the medical records of the patients. The following are data elements that the doctor should be able to view in order to complete their tasks:
● The table of patient information in order to view relevant patient information; as in creating the patient's medical record
● The table of test information in order to update a test entry for a patient.
● The table of staff information in order to update or create new entries.
● The table of medical records in order to create and update patients records as
necessary. This includes viewing a patient’s tests, prescriptions and diagnosis details.
A doctor does not need to be able to view check-in information, ward information or billing accounts.

**6.3. Nurse**
Nurses are responsible for treating patients and are responsible for specific wards. The following are data elements that the nurse should be able to view in order to complete their tasks:
  ● The table of patient information in order to view relevant patient information.
  ● The table of ward information in order to check information on their responsible
  wards and coordinate with other nurses.
  ● The table of staff information in order to update or create new entries.

We don’t find it necessary for nurses to update medical records as we assume they are just tending to daily routine needs of patients in the wards. It is also unnecessary for them to view check-in information or billing accounts.
