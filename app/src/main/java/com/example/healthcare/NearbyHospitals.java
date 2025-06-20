package com.example.healthcare;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NearbyHospitals extends AppCompatActivity {

    Spinner stateSpinner;
    ListView cityListView;
    ImageView emptyImage;
    Map<String, List<String>> stateCityMap = new HashMap<>();
    Map<String, List<Hospital>> cityHospitalMap = new HashMap<>();
    ArrayAdapter<String> cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_hospitals);

        stateSpinner = findViewById(R.id.stateSpinner);
        cityListView = findViewById(R.id.cityListView);
        emptyImage = findViewById(R.id.emptyImage);

        // Populate states and cities
        populateStateCityMap();
        populateCityHospitalMap();

        // Setup spinner with state names
        List<String> states = new ArrayList<>();
        states.add("SELECT"); // Add default item at the top
        List<String> filteredStates = new ArrayList<>();
        for (String state : stateCityMap.keySet()) {
            if (!state.equals("SELECT")) {
                filteredStates.add(state);
            }
        }
        Collections.sort(filteredStates);
        states.addAll(filteredStates);

        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, states);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(stateAdapter);

// Optional: Set default selection to "SELECT"
        stateSpinner.setSelection(0);

// Spinner selection listener
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedState = states.get(i);
                List<String> cities = new ArrayList<>();

                if (!selectedState.equals("SELECT")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        cities = stateCityMap.getOrDefault(selectedState, new ArrayList<>());
                    }
                }

                cityAdapter = new ArrayAdapter<>(NearbyHospitals.this, android.R.layout.simple_list_item_1, cities);
                cityListView.setAdapter(cityAdapter);

                // Toggle visibility
                if (cities.isEmpty()) {
                    cityListView.setVisibility(View.GONE);
                    emptyImage.setVisibility(View.VISIBLE);
                } else {
                    cityListView.setVisibility(View.VISIBLE);
                    emptyImage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        // City tap listener
        cityListView.setOnItemClickListener((adapterView, view, i, l) -> {
            String city = cityAdapter.getItem(i);
            showHospitalDialog(city);
        });
    }

    private void showHospitalDialog(String cityName) {
        List<Hospital> hospitals = cityHospitalMap.getOrDefault(cityName, new ArrayList<>());

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_hospital_list, null);
        EditText searchInput = dialogView.findViewById(R.id.searchHospital);
        ListView hospitalListView = dialogView.findViewById(R.id.hospitalList);
        ImageView emptyHospitalImage = dialogView.findViewById(R.id.emptyHospitalImage);

        ArrayAdapter<Hospital> hospitalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>(hospitals));
        hospitalListView.setAdapter(hospitalAdapter);

        // Toggle visibility for hospitals
        if (hospitals.isEmpty()) {
            hospitalListView.setVisibility(View.GONE);
            emptyHospitalImage.setVisibility(View.VISIBLE);
        } else {
            hospitalListView.setVisibility(View.VISIBLE);
            emptyHospitalImage.setVisibility(View.GONE);
        }

        searchInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                hospitalAdapter.getFilter().filter(s);
            }
            @Override public void afterTextChanged(android.text.Editable s) {}
        });

        android.app.AlertDialog hospitalDialog = new android.app.AlertDialog.Builder(this)
                .setTitle("Hospitals in " + cityName)
                .setView(dialogView)
                .setNegativeButton("Close", null)
                .show();

        hospitalListView.setOnItemClickListener((parent, view, position, id) -> {
            Hospital selectedHospital = hospitalAdapter.getItem(position);
            showHospitalDetailsDialog(selectedHospital);
        });
    }

    private void showHospitalDetailsDialog(Hospital hospital) {
        String message = "Name: " + hospital.name + "\n\n"
                + "Contact: " + hospital.contact + "\n\n"
                + "Address: " + hospital.address + "\n\n"
                + "Expertise: " + hospital.expertise + "\n\n"
                + "Other Details: " + hospital.otherDetails;

        new android.app.AlertDialog.Builder(this)
                .setTitle("Hospital Details")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private void populateCityHospitalMap() {
        cityHospitalMap.put("Chennai", List.of(
                new Hospital("Apollo Hospital, Greams Road", "044-2829 3333", "Greams Road, Chennai", "Multi-speciality", "24/7 Emergency, International patient care"),
                new Hospital("MIOT International", "044-4200 2288", "Manapakkam, Chennai", "Orthopaedics, Cardiology", "Known for transplant surgery"),
                new Hospital("Fortis Malar Hospital", "044-4289 2222", "Adyar, Chennai", "Neurology, Cardiology", "Accredited with NABH"),
                new Hospital("Gleneagles Global Health City", "044-4477 7000", "Perumbakkam, Chennai", "Liver Transplant, Oncology", "JCI Accredited"),
                new Hospital("Sri Ramachandra Medical Centre", "044-4592 8500", "Porur, Chennai", "General Medicine, Surgery", "Teaching hospital with research focus"),
                new Hospital("Dr. Mehta’s Hospitals", "044-4227 2222", "Chetpet, Chennai", "Paediatrics, Women's Health", "One of Chennai's oldest hospitals"),
                new Hospital("Billroth Hospitals", "044-4292 1000", "Shenoy Nagar, Chennai", "General Surgery, Cardiology", "24/7 ICU and trauma care"),
                new Hospital("Vijaya Hospital", "044-2480 1000", "Vadapalani, Chennai", "Nephrology, ENT", "Advanced Dialysis & ENT units")
        ));

        cityHospitalMap.put("Coimbatore", List.of(
                new Hospital("Ganga Hospital", "0422-2485000", "Mettupalayam Road, Coimbatore", "Orthopaedics, Plastic Surgery", "Renowned for trauma care"),
                new Hospital("KMCH (Kovai Medical Center & Hospital)", "0422-4323800", "Avanashi Road, Coimbatore", "Multi-speciality", "NABH accredited, 1000+ beds"),
                new Hospital("PSG Hospitals", "0422-2570170", "Peelamedu, Coimbatore", "General Medicine, Cardiology", "Medical college hospital"),
                new Hospital("Sri Ramakrishna Hospital", "0422-2485000", "Sidhapudur, Coimbatore", "Oncology, Neurology", "High-end diagnostics and 24/7 emergency"),
                new Hospital("Kovai Medical Center (Women & Child)", "0422-432 3000", "Avanashi Road, Coimbatore", "Gynaecology, Neonatology", "Advanced neonatal ICU"),
                new Hospital("Lotus Eye Hospital", "0422-4229800", "Peelamedu, Coimbatore", "Ophthalmology", "Eye care superspeciality"),
                new Hospital("Rao Hospital", "0422-2231234", "R.S. Puram, Coimbatore", "Fertility, Women's Health", "Pioneers in infertility treatment in Tamil Nadu"),
                new Hospital("Ortho One Hospital", "0422-4055100", "Nehru Nagar, Coimbatore", "Orthopaedics, Sports Injury", "Joint replacement and arthroscopy specialists")
        ));

        cityHospitalMap.put("New Delhi", List.of(
                new Hospital("AIIMS (All India Institute of Medical Sciences)", "011-26588500", "Ansari Nagar, New Delhi", "Multi-speciality, Research", "Premier Govt. medical institute in India"),
                new Hospital("Max Super Speciality Hospital, Patparganj", "011-43033333", "Patparganj, New Delhi", "Cardiology, Oncology", "NABH accredited, advanced robotic surgery"),
                new Hospital("Fortis Escorts Heart Institute", "011-47135000", "Okhla Road, New Delhi", "Cardiology, Cardiac Surgery", "Top cardiac care center in India"),
                new Hospital("BLK-Max Super Speciality Hospital", "011-30403040", "Pusa Road, New Delhi", "Oncology, Bone Marrow Transplant", "International patient care, NABH"),
                new Hospital("Indraprastha Apollo Hospital", "011-71791090", "Sarita Vihar, New Delhi", "Organ Transplant, Neurology", "JCI accredited, 700+ beds"),
                new Hospital("Sir Ganga Ram Hospital", "011-25750000", "Old Rajinder Nagar, New Delhi", "General Medicine, Paediatrics", "Multi-speciality teaching hospital"),
                new Hospital("Dr. Ram Manohar Lohia Hospital", "011-23365525", "Connaught Place, New Delhi", "General Surgery, Psychiatry", "Central Govt. hospital with wide access"),
                new Hospital("Moolchand Medcity", "011-42000000", "Lajpat Nagar III, New Delhi", "Gastroenterology, Fertility", "One of Delhi’s oldest private hospitals")
        ));

        cityHospitalMap.put("Dehradun", List.of(
                new Hospital("Max Super Speciality Hospital", "0135-6673000", "Mussoorie Diversion Road, Dehradun", "Cardiology, Neurosciences", "Top NABH-accredited hospital in Uttarakhand"),
                new Hospital("Synergy Institute of Medical Sciences", "0135-6677777", "Ballupur, Dehradun", "Orthopaedics, ICU", "Modern trauma care unit"),
                new Hospital("Doon Hospital", "0135-2655271", "Parade Ground, Dehradun", "General Medicine", "Govt. run multi-speciality hospital"),
                new Hospital("Velmed Hospital", "0135-6670999", "GMS Road, Dehradun", "General Surgery, Cardiology", "Affordable quality care"),
                new Hospital("Combined Medical Institute (CMI)", "0135-2749271", "Haridwar Road, Dehradun", "Gynecology, Orthopaedics", "Well-known for maternity and ortho"),
                new Hospital("Doon Valley Hospital", "0135-2672727", "Haridwar Bypass, Dehradun", "ENT, Dermatology", "24x7 emergency services"),
                new Hospital("Shri Mahant Indiresh Hospital", "0135-6603000", "Subhash Road, Dehradun", "Multi-speciality", "Teaching hospital under SRHU"),
                new Hospital("City Heart Centre", "0135-2727071", "Race Course, Dehradun", "Cardiology", "Cardiac diagnostics and treatments")
        ));

        cityHospitalMap.put("Saket", List.of(
                new Hospital("Max Super Speciality Hospital", "011-26515050", "Press Enclave Road, Saket", "Multi-speciality", "Internationally renowned for cancer care, neurology"),
                new Hospital("Saket City Hospital (Now Max Smart Super Speciality)", "011-47672000", "Mandir Marg, Saket", "Cardiology, Orthopaedics", "JCI & NABH accredited"),
                new Hospital("Rainbow Children's Hospital", "011-43008400", "Malviya Nagar, Saket", "Pediatrics, Neonatology", "Specialized in child care and NICU"),
                new Hospital("Pushpawati Singhania Hospital & Research Institute", "011-30611700", "Sheikh Sarai Phase 1, Saket", "Gastroenterology, Nephrology", "Dedicated GI & liver center"),
                new Hospital("Healing Touch Hospital", "011-26852244", "Geetanjali Enclave, Saket", "General Surgery, ENT", "Family & general care"),
                new Hospital("Aakash Hospital", "011-40847777", "Malviya Nagar, near Saket", "Internal Medicine, Diabetology", "Mid-size multi-speciality hospital"),
                new Hospital("Cygnus Orthocare Hospital", "011-47107777", "Safdarjung Enclave (close to Saket)", "Orthopaedics, Sports Injuries", "Specialized joint replacement center"),
                new Hospital("Chikitsa Hospital", "011-26653871", "Saket Community Centre, Saket", "General Medicine, Surgery", "Affordable care with good diagnostics")
        ));

        cityHospitalMap.put("Lucknow", List.of(
                new Hospital("Sanjay Gandhi Postgraduate Institute of Medical Sciences (SGPGI)", "0522-2668700", "Raebareli Road, Lucknow", "Nephrology, Cardiology, Liver Transplant", "Premier govt. research hospital"),
                new Hospital("King George's Medical University (KGMU)", "0522-2257540", "Chowk, Lucknow", "Trauma, Pediatrics, Surgery", "Historical institution, govt. tertiary care"),
                new Hospital("Medanta Hospital", "0522-4505050", "Amar Shaheed Path, Lucknow", "Oncology, Neurology, Cardiac Sciences", "World-class private care"),
                new Hospital("Apollomedics Super Speciality Hospital", "0522-6788888", "Kanpur Road, Lucknow", "Multi-speciality", "JCI standards, 24x7 emergency"),
                new Hospital("Ram Manohar Lohia Institute of Medical Sciences", "0522-4918555", "Vibhuti Khand, Gomti Nagar, Lucknow", "General Surgery, Gastroenterology", "Govt. funded super-speciality hospital"),
                new Hospital("Charak Hospital & Research Centre", "0522-4032000", "Dubagga, Lucknow", "Critical Care, ENT, Pulmonology", "Advanced trauma and intensive care"),
                new Hospital("Shekhar Hospital", "0522-4042888", "Indira Nagar, Lucknow", "Gynaecology, Orthopaedics", "Well-known maternity & ortho center"),
                new Hospital("Sahara Hospital", "0522-6780001", "Gomti Nagar, Lucknow", "Cardiology, Oncology", "Modern private facility")
        ));

        cityHospitalMap.put("Patna", List.of(
                new Hospital("Indira Gandhi Institute of Medical Sciences (IGIMS)", "0612-2297099", "Sheikhpura, Patna", "Oncology, Neurology", "Autonomous institute under Govt. of Bihar"),
                new Hospital("AIIMS Patna", "0612-2451923", "Phulwari Sharif, Patna", "Multi-speciality, Emergency Care", "Premier govt. institute"),
                new Hospital("Paras HMRI Hospital", "0612-7107777", "Bailey Road, Patna", "Cardiology, Urology", "Largest private hospital in Bihar"),
                new Hospital("Ruban Memorial Hospital", "0612-2293181", "Patliputra Colony, Patna", "Critical Care, Gastroenterology", "24x7 trauma and ambulance support"),
                new Hospital("Ford Hospital & Research Centre", "0612-2233233", "New Bypass Road, Patna", "Orthopaedics, General Surgery", "Affordable super-speciality care"),
                new Hospital("MGM Hospital", "0612-2352425", "Kankarbagh, Patna", "Paediatrics, Diabetology", "Known for maternal and child health"),
                new Hospital("Big Apollo Spectra Hospital", "0612-2353787", "Fraser Road Area, Patna", "General Medicine, ENT", "Branch of Apollo Spectra Hospitals"),
                new Hospital("Holy Family Hospital", "0612-2671351", "Kurji, Patna", "Obstetrics, General Surgery", "Catholic missionary hospital with quality care")
        ));

        cityHospitalMap.put("Darbhanga", List.of(
                new Hospital("Darbhanga Medical College and Hospital (DMCH)", "06272-245293", "Laheriasarai, Darbhanga", "General Medicine, Orthopaedics", "Major govt. teaching hospital in North Bihar"),
                new Hospital("Mithila Multispeciality Hospital", "09031615177", "Laheriasarai, Darbhanga", "Cardiology, Neurology", "Well-equipped private care unit"),
                new Hospital("Care India Hospital", "09125518598", "VIP Road, Darbhanga", "Gynaecology, Pediatrics", "Affordable maternity services"),
                new Hospital("Shree Sai Hospital", "09386122999", "Station Road, Darbhanga", "General Surgery, ENT", "Day care and surgical unit"),
                new Hospital("Shubham Hospital", "09031781371", "Benta Chowk, Darbhanga", "Orthopaedics, Trauma", "24x7 emergency & ICU support"),
                new Hospital("Aman Hospital", "08252601103", "Donar Road, Darbhanga", "General Medicine, Pathology", "Mid-size general hospital"),
                new Hospital("Saket Hospital", "07488887888", "Allalpatti, Darbhanga", "Gastroenterology, General Surgery", "Diagnostic and surgical services"),
                new Hospital("BMIMS Hospital", "08757383888", "Baheda Road, Darbhanga", "General, Gynae, Ortho", "Connected to medical institute for training & care")
        ));

        cityHospitalMap.put("Hyderabad", List.of(
                new Hospital("Apollo Hospitals", "040-23607777", "Jubilee Hills, Hyderabad", "Cardiology, Oncology, Neurology", "Flagship multispeciality hospital"),
                new Hospital("Yashoda Hospitals", "040-45674567", "Somajiguda, Hyderabad", "Oncology, Emergency, Organ Transplant", "Renowned for critical care & cancer treatment"),
                new Hospital("CARE Hospitals", "040-67202200", "Banjara Hills, Hyderabad", "Cardiac Sciences, Nephrology", "Pioneers in cardiac care in South India"),
                new Hospital("KIMS Hospital", "040-49405000", "Begumpet, Hyderabad", "Orthopaedics, Liver Transplant", "State-of-the-art operation theatres"),
                new Hospital("Continental Hospitals", "040-67000000", "Financial District, Hyderabad", "Gastroenterology, Urology, Emergency", "International standards, NABH accredited"),
                new Hospital("AIG Hospitals", "040-42446666", "Gachibowli, Hyderabad", "Gastroenterology, Hepatology", "Asia’s largest gastroenterology institute"),
                new Hospital("Basavatarakam Indo American Cancer Hospital", "040-23551200", "Banjara Hills, Hyderabad", "Oncology", "Specialized cancer research and treatment center"),
                new Hospital("Rainbow Children’s Hospital", "040-44665555", "Banjara Hills, Hyderabad", "Pediatrics, Neonatology", "Top-ranked children’s hospital in India")
        ));

        cityHospitalMap.put("Nizamabad", List.of(
                new Hospital("Government General Hospital (GGH)", "08462-224444", "Khaleelwadi, Nizamabad", "General Medicine, Surgery", "Main govt. hospital serving Nizamabad"),
                new Hospital("Prathima Hospitals", "08462-234567", "Yellammagutta, Nizamabad", "Cardiology, Orthopaedics", "Modern facility with 24x7 emergency care"),
                new Hospital("Sri Sai Multispeciality Hospital", "08462-223355", "Bodhan Road, Nizamabad", "General Surgery, ENT, Pediatrics", "Affordable multi-department hospital"),
                new Hospital("Sanjeevani Hospital", "08462-221122", "Beside Padmanayaka Kalyana Mandapam, Nizamabad", "Neurology, ICU Care", "Known for critical care services"),
                new Hospital("Sri Krishna Neuro and Multispeciality Hospital", "08462-220555", "Armoor Road, Nizamabad", "Neurology, Trauma", "Specialized in neurological care"),
                new Hospital("Balaji Hospital", "08462-225500", "Khaleelwadi, Nizamabad", "Gynecology, General Medicine", "Popular among maternity care patients"),
                new Hospital("Vasavi Hospital", "08462-226789", "Chandrashekar Colony, Nizamabad", "Diabetology, Cardiology", "Good reputation for diabetes and heart patients"),
                new Hospital("Surya Hospital", "08462-235678", "Subhash Nagar, Nizamabad", "Orthopaedics, Radiology", "Mid-sized hospital with in-house diagnostics")
        ));

    }

    private void populateStateCityMap() {
        stateCityMap.put("SELECT", List.of());
        stateCityMap.put("Uttar Pradesh", List.of("Lucknow", "Kanpur", "Prayagraj", "Varanasi", "Agra", "Ghaziabad"));
        stateCityMap.put("Maharashtra", List.of("Mumbai", "Pune", "Nagpur", "Nashik", "Thane", "Aurangabad"));
        stateCityMap.put("Tamil Nadu", List.of("Chennai", "Coimbatore", "Madurai", "Salem", "Tiruchirappalli", "Erode"));
        stateCityMap.put("Karnataka", List.of("Bengaluru", "Mysuru", "Hubballi", "Belagavi", "Mangaluru", "Shivamogga"));
        stateCityMap.put("Delhi", List.of("New Delhi", "Dwarka", "Karol Bagh", "Rohini", "Saket", "Lajpat Nagar"));
        stateCityMap.put("West Bengal", List.of("Kolkata", "Howrah", "Durgapur", "Asansol", "Siliguri", "Kharagpur"));
        stateCityMap.put("Rajasthan", List.of("Jaipur", "Jodhpur", "Udaipur", "Kota", "Ajmer", "Bikaner"));
        stateCityMap.put("Gujarat", List.of("Ahmedabad", "Surat", "Vadodara", "Rajkot", "Gandhinagar", "Bhavnagar"));
        stateCityMap.put("Punjab", List.of("Ludhiana", "Amritsar", "Jalandhar", "Patiala", "Bathinda", "Mohali"));
        stateCityMap.put("Haryana", List.of("Gurgaon", "Faridabad", "Panipat", "Ambala", "Rohtak", "Hisar"));
        stateCityMap.put("Madhya Pradesh", List.of("Bhopal", "Indore", "Jabalpur", "Gwalior", "Ujjain", "Sagar"));
        stateCityMap.put("Bihar", List.of("Patna", "Gaya", "Muzaffarpur", "Bhagalpur", "Darbhanga", "Purnia"));
        stateCityMap.put("Jharkhand", List.of("Ranchi", "Jamshedpur", "Dhanbad", "Bokaro", "Hazaribagh", "Deoghar"));
        stateCityMap.put("Odisha", List.of("Bhubaneswar", "Cuttack", "Rourkela", "Puri", "Sambalpur", "Berhampur"));
        stateCityMap.put("Kerala", List.of("Thiruvananthapuram", "Kochi", "Kozhikode", "Thrissur", "Kannur", "Alappuzha"));
        stateCityMap.put("Andhra Pradesh", List.of("Visakhapatnam", "Vijayawada", "Guntur", "Nellore", "Tirupati", "Kurnool"));
        stateCityMap.put("Telangana", List.of("Hyderabad", "Warangal", "Nizamabad", "Karimnagar", "Khammam", "Ramagundam"));
        stateCityMap.put("Chhattisgarh", List.of("Raipur", "Bhilai", "Durg", "Bilaspur", "Korba", "Raigarh"));
        stateCityMap.put("Assam", List.of("Guwahati", "Dibrugarh", "Silchar", "Jorhat", "Tezpur", "Tinsukia"));
        stateCityMap.put("Goa", List.of("Panaji", "Margao", "Vasco da Gama", "Mapusa", "Ponda"));
        stateCityMap.put("Himachal Pradesh", List.of("Shimla", "Manali", "Dharamshala", "Solan", "Mandi", "Bilaspur"));
        stateCityMap.put("Uttarakhand", List.of("Dehradun", "Haridwar", "Rishikesh", "Haldwani", "Roorkee", "Nainital"));
        stateCityMap.put("Jammu and Kashmir", List.of("Srinagar", "Jammu", "Anantnag", "Baramulla", "Udhampur", "Kathua"));
        stateCityMap.put("Tripura", List.of("Agartala", "Udaipur", "Dharmanagar", "Kailasahar", "Belonia"));
        stateCityMap.put("Meghalaya", List.of("Shillong", "Tura", "Jowai", "Nongpoh", "Baghmara"));
    }
}
