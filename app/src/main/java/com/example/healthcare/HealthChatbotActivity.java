package com.example.healthcare;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import java.util.*;

public class HealthChatbotActivity extends AppCompatActivity {

    private ListView chatListView;
    private EditText userMessage;
    private Button sendBtn;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private Handler handler = new Handler(); // For delay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_chatbot);

        chatListView = findViewById(R.id.chatListView);
        userMessage = findViewById(R.id.userMessage);
        sendBtn = findViewById(R.id.sendBtn);

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, chatMessages);
        chatListView.setAdapter(chatAdapter);

        sendBtn.setOnClickListener(v -> {
            String userInput = userMessage.getText().toString().trim();
            if (!TextUtils.isEmpty(userInput)) {
                addMessage(userInput, true); // User message
                userMessage.setText("");

                // Add temporary "typing" indicator
                addMessage("Bot is typing...", false);
                chatListView.smoothScrollToPosition(chatMessages.size() - 1);

                // Simulate delay before bot response
                handler.postDelayed(() -> {
                    // Remove "typing..." message
                    chatMessages.remove(chatMessages.size() - 1);
                    chatAdapter.notifyDataSetChanged();

                    // Add actual bot reply
                    String response = getBotResponse(userInput.toLowerCase());
                    addMessage(response, false);
                    chatListView.smoothScrollToPosition(chatMessages.size() - 1);
                }, 1200); // 1.2 seconds delay
            }
        });
    }

    private void addMessage(String msg, boolean isUser) {
        chatMessages.add(new ChatMessage(msg, isUser));
        chatAdapter.notifyDataSetChanged();
    }

    private String getBotResponse(String input) {
        input = input.toLowerCase();

        // Greetings
        if (input.contains("hello") || input.contains("hi") || input.contains("hey"))
            return "Hi! I’m your HealthBot 🤖. Ask me anything health-related!";
        if (input.contains("how are you"))
            return "I'm here to help you stay healthy! What can I assist you with today?";

        // Common Illnesses
        if (input.contains("fever"))
            return "Fever may indicate infection. Stay hydrated and rest. If high or persistent, consult a doctor.";
        if (input.contains("cold") || input.contains("cough"))
            return "Colds often go away in a few days. Use warm fluids and rest. See a doctor if it worsens.";
        if (input.contains("headache"))
            return "Headaches can be due to stress, dehydration, or eye strain. Try rest, hydration, or a cold compress.";
        if (input.contains("vomiting"))
            return "Vomiting may result from infection or food issues. Keep hydrated with ORS. Consult if persistent.";

        // Chronic Conditions
        if (input.contains("diabetes"))
            return "Manage diabetes with proper diet, regular exercise, blood sugar monitoring, and medication.";
        if (input.contains("hypertension") || input.contains("blood pressure") || input.contains("bp"))
            return "Maintain BP with less salt, regular walks, and stress control. Normal is ~120/80 mmHg.";
        if (input.contains("asthma"))
            return "Asthma symptoms include breathlessness and wheezing. Use inhalers as prescribed and avoid triggers.";
        if (input.contains("cholesterol"))
            return "Lower cholesterol by avoiding fried food and increasing fiber (oats, fruits, veggies).";

        // Women's Health
        if (input.contains("period") || input.contains("menstrual"))
            return "Periods last 3–7 days monthly. Irregular cycles can be due to stress or hormonal issues.";
        if (input.contains("pregnancy") || input.contains("pregnant"))
            return "Pregnant women should eat healthy, avoid alcohol/smoking, take folic acid, and visit doctors regularly.";
        if (input.contains("pcos"))
            return "PCOS can cause irregular periods, acne, weight gain. It's manageable through diet and lifestyle.";

        // Mental Health
        if (input.contains("mental") || input.contains("anxiety") || input.contains("depression"))
            return "Mental health matters! Seek support from friends, counselors, and get adequate rest and nutrition.";
        if (input.contains("stress"))
            return "Stress can affect health. Try breathing exercises, nature walks, or mindful meditation.";

        // Lifestyle
        if (input.contains("nutrition") || input.contains("diet"))
            return "Balanced diet includes carbs, protein, fiber, fats, and vitamins. Limit processed and sugary foods.";
        if (input.contains("exercise") || input.contains("workout"))
            return "30 mins of daily exercise keeps your body and mind fit. Try walking, yoga, or any sport.";
        if (input.contains("hydration") || input.contains("water"))
            return "Drink 2–3 liters of water daily to maintain body functions and energy.";
        if (input.contains("sleep"))
            return "7–9 hours of sleep is recommended. Stick to a schedule and avoid screens before bed.";

        // Skin, Hair, Eyes
        if (input.contains("skin care") || input.contains("acne"))
            return "Cleanse twice daily, stay hydrated, and avoid greasy food. Consult a dermatologist if needed.";
        if (input.contains("hair loss"))
            return "Hair loss may be due to stress, deficiency, or genetics. Eat protein-rich food and stay healthy.";
        if (input.contains("eye strain") || input.contains("vision"))
            return "Reduce screen time. Use the 20-20-20 rule: Every 20 mins, look 20 ft away for 20 seconds.";

        // Children’s Health
        if (input.contains("child") || input.contains("baby"))
            return "Ensure timely vaccinations, proper nutrition, and regular pediatric checkups.";
        if (input.contains("vaccination"))
            return "Vaccines prevent serious diseases. Stick to the national immunization schedule.";

        // COVID-19
        if (input.contains("covid") || input.contains("corona"))
            return "COVID-19 spreads via droplets. Wear masks, stay distanced, and follow local health updates.";
        if (input.contains("covid vaccine"))
            return "COVID vaccines are safe and reduce severity. Booster doses are advised for many groups.";

        // Miscellaneous
        if (input.contains("fatigue") || input.contains("tired"))
            return "Fatigue may result from anemia, stress, or poor sleep. Get tested if it's frequent.";
        if (input.contains("weight loss"))
            return "Weight loss = calories burned > calories consumed. Avoid crash diets; aim for gradual results.";
        if (input.contains("thyroid"))
            return "Thyroid issues can affect weight and energy. Blood tests help diagnose it. Medication helps.";
        if (input.contains("first aid"))
            return "For minor cuts: clean with water, apply antiseptic, and cover. Seek medical help for deep wounds.";
        if (input.contains("burn"))
            return "Cool the burn under water, apply aloe vera or gel. Don't use ice or butter on burns.";

        if (input.contains("hello") || input.contains("hi"))
            return "Hi! I’m your HealthBot 🤖. Ask me anything health-related!";
        if (input.contains("covid") || input.contains("corona"))
            return "COVID-19 symptoms include fever, cough, fatigue, and loss of taste. Follow masking and vaccination guidelines.";
        if (input.contains("fever"))
            return "Fever usually means your body is fighting an infection. Monitor your temperature and hydrate well.";
        if (input.contains("headache"))
            return "Headaches can be caused by stress, screen time, dehydration, or sleep issues.";
        if (input.contains("cold") || input.contains("cough"))
            return "Rest, fluids, and warm soup help with colds. See a doctor if symptoms last more than 7 days.";
        if (input.contains("diabetes"))
            return "Diabetes is a chronic condition. Eat balanced meals, exercise, and monitor your blood sugar regularly.";
        if (input.contains("blood pressure") || input.contains("bp"))
            return "Normal BP is around 120/80 mmHg. High BP can lead to heart risks, so limit salt and stress.";
        if (input.contains("heart"))
            return "Heart health tips: avoid smoking, eat healthy fats, exercise regularly, and monitor cholesterol.";
        if (input.contains("pregnant") || input.contains("pregnancy"))
            return "Pregnant women should take folic acid, avoid alcohol/smoking, and have regular prenatal checkups.";
        if (input.contains("period") || input.contains("menstrual"))
            return "Menstrual cycles vary but average around 28 days. Irregularities can be due to stress or health issues.";
        if (input.contains("mental health") || input.contains("anxiety") || input.contains("depression"))
            return "Mental health is as important as physical. Talk to a counselor, and prioritize sleep, rest, and support.";
        if (input.contains("nutrition") || input.contains("vitamins"))
            return "Balanced nutrition includes proteins, vitamins, healthy fats, and whole grains. Avoid junk food.";
        if (input.contains("water") || input.contains("hydration"))
            return "Drink at least 2–3 liters of water daily. Hydration helps digestion, skin, and energy levels.";
        if (input.contains("exercise") || input.contains("workout"))
            return "Try 30 minutes of daily exercise — walk, run, yoga, or strength training — for better health.";
        if (input.contains("weight loss"))
            return "Weight loss is best with a calorie deficit — eat smart, move more, and stay consistent.";
        if (input.contains("cholesterol"))
            return "High cholesterol? Avoid fried food and increase fiber-rich foods like oats, fruits, and veggies.";
        if (input.contains("sleep"))
            return "Adults need 7–9 hours of sleep. Keep a routine, limit screen time before bed, and avoid caffeine late.";
        if (input.contains("skin care") || input.contains("acne"))
            return "Wash face twice daily, drink water, and avoid oily foods. See a dermatologist for persistent acne.";
        if (input.contains("eye strain") || input.contains("vision"))
            return "Follow the 20-20-20 rule for screens: every 20 mins, look 20 feet away for 20 seconds.";
        if (input.contains("fatigue") || input.contains("tired"))
            return "Fatigue can result from stress, sleep deprivation, anemia, or thyroid issues. Consider a health checkup.";
        if (input.contains("child") || input.contains("baby"))
            return "Children need vaccinations, nutrition, and lots of love and care. Regular checkups are key.";
        if (input.contains("vaccination"))
            return "Vaccines protect against serious diseases. Follow your country’s immunization schedule.";
        if (input.contains("covid vaccine"))
            return "COVID-19 vaccines reduce severe illness. Booster doses may be advised for elderly or high-risk people.";
        if (input.contains("burn"))
            return "For minor burns, cool the area under running water and apply aloe vera. Seek care for severe burns.";
        if (input.contains("allergy"))
            return "Common allergies include pollen, dust, and food. Antihistamines help, but see an allergist for severe cases.";
        if (input.contains("asthma"))
            return "Asthma symptoms include wheezing and shortness of breath. Use inhalers as prescribed and avoid triggers.";
        if (input.contains("first aid"))
            return "First aid involves cleaning wounds, applying pressure to bleeding, and calling emergency services if needed.";
        if (input.contains("cancer"))
            return "Early detection is key. Screen regularly and consult a doctor if you notice unusual lumps or symptoms.";
        if (input.contains("arthritis"))
            return "Arthritis causes joint pain and stiffness. Regular movement and medication can manage it.";
        if (input.contains("thyroid"))
            return "Thyroid disorders can affect weight, mood, and energy. A blood test can check thyroid hormone levels.";
        if (input.contains("back pain"))
            return "Back pain can result from posture or injury. Try stretching, physiotherapy, or medical consultation.";
        if (input.contains("hair loss"))
            return "Hair loss can be due to genetics, stress, or deficiency. Consult a dermatologist for diagnosis and treatment.";

        return "I'm not sure about that. Please consult a certified doctor for accurate advice. Please call 108 / 102 in case of Emergency!";
    }
}
