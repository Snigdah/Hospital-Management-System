package com.example.recommendation.service.impl;

import com.example.recommendation.dto.response.ConsultationResponseDTO;
import com.example.recommendation.entity.enums.ConsultationStage;
import com.example.recommendation.service.IMedicalConsultationService;
import org.springframework.stereotype.Service;

@Service
public class MedicalConsultationService implements IMedicalConsultationService {
    private ConsultationStage currentStage = ConsultationStage.START;
    public ConsultationResponseDTO getNextStep(String userResponse) {
        switch (currentStage) {
            case START -> {
                currentStage = ConsultationStage.FIRST_QUESTION;
                return new ConsultationResponseDTO("What is your problem? 'Mental' or 'Physical' ", false);
            }
            case FIRST_QUESTION -> {
                if ("mental".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.SECOND_QUESTION;
                    return new ConsultationResponseDTO("Are you experiencing 'Stress', 'Anxiety', 'Sleep' disturbances? or 'others' ?", false);
                } else if ("physical".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.SECOND_QUESTION;
                    return new ConsultationResponseDTO("Is your problem 'internal' or an 'injury' ?", false);
                }
            }
            case SECOND_QUESTION -> {
                if ("internal".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.THIRD_QUESTION;
                    return new ConsultationResponseDTO("Do you have a 'Fever' or 'Cold' symptoms?", false);
                } else if ("injury".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.THIRD_QUESTION;
                    return new ConsultationResponseDTO("Is the injury a result of a 'Recent' Accident or is it a 'Recurring' issue?", false);
                } else if ("stress".equalsIgnoreCase(userResponse) || "Anxiety".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.THIRD_QUESTION;
                    return new ConsultationResponseDTO("Is it 'Long' or 'Short' period of time ?", false);
                } else if ("sleep".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.THIRD_QUESTION;
                    return new ConsultationResponseDTO("Did you previously go doctor or take medicine ('Yes' / 'NO') ?", false);
                } else if ("others".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.END;
                    return new ConsultationResponseDTO("You should go to 'Neurology Department'.", true);
                }
            }
            case THIRD_QUESTION -> {
                if ("fever".equalsIgnoreCase(userResponse) || "Cold".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.END;
                    return new ConsultationResponseDTO("Based on your symptoms, you should visit a medicine doctor.", true);
                } else if ("yes".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.FOURTH_QUESTION;
                    return new ConsultationResponseDTO("Are you currently following any treatment or medication regimen prescribed by your doctor? ('Yes' / 'No'", false);
                } else if ("no".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.END;
                    return new ConsultationResponseDTO("Consult with a sleep specialist for alternative treatment options..", true);
                } else if ("short".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.END;
                    return new ConsultationResponseDTO("Observation and stress management techniques may be helpful. If symptoms worsen, please consult a healthcare provider.", true);
                } else if ("long".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.FOURTH_QUESTION;
                    return new ConsultationResponseDTO("Would you describe your condition as 'Chronic' or 'Recurrent'? ", false);
                } else if ("recurring".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.END;
                    return new ConsultationResponseDTO("You should receive any prior treatment for this injury, such as 'Physiotherapy' or 'Surgery' or your previous doctor ", true);
                } else if ("recent".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.FOURTH_QUESTION;
                    return new ConsultationResponseDTO("Are you experiencing 'Severe Pain', 'Swelling', or 'Restricted Movement' in the injured area? ", false);
                }
            }
            case FOURTH_QUESTION -> {
                if ("chronic".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.END;
                    return new ConsultationResponseDTO(" For chronic physical conditions, seeing a specialist in the respective area (like cardiology, endocrinology, etc.) is recommended.", true);
                } else if ("no".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.END;
                    return new ConsultationResponseDTO("You should go 'Physiologist' ", true);
                } else if ("severe pain".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.FIFTH_QUESTION;
                    return new ConsultationResponseDTO("Is the pain 'Localized' to one area or 'Radiating' to other parts of the body? ", false);
                } else if ("yes".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.END;
                    return new ConsultationResponseDTO("You should go your previous doctor again. For better you can go 'Abnormal psychology Department' ", true);
                } else if ("swelling".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.FIFTH_QUESTION;
                    return new ConsultationResponseDTO("\"Has the swelling been increasing since the injury occurred? ('YES' / 'NO') ", false);
                } else if ("restricted movement".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.FIFTH_QUESTION;
                    return new ConsultationResponseDTO("Is the restriction in movement accompanied by 'Severe Pain' or 'Inability' to Use the Limb ? ", false);
                } else if ("recurrent".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.END;
                    return new ConsultationResponseDTO("You should go your previous doctor again. For better you can go 'Abnormal psychology Department' ", true);
                }
            }
            case FIFTH_QUESTION -> {
                if ("localized".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.END;
                    return new ConsultationResponseDTO("This could indicate a serious injury. Immediate medical attention in an emergency department is advised. ", true);
                } else if ("radiating".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.SIX_QUESTION;
                    return new ConsultationResponseDTO("Is the radiating pain accompanied by any 'Numbness', or  Loss of 'Movement' in any limb? ", false);
                } else if ("yes".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.END;
                    return new ConsultationResponseDTO("It's important to have the swelling evaluated, especially if it's increasing. A visit to an urgent care center or your healthcare provider is advisable. ", true);
                } else if ("no".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.END;
                    return new ConsultationResponseDTO("Monitor the swelling. If it doesn't decrease or is accompanied by increasing pain or other symptoms, seek medical attention. ", true);
                } else if ("severe pain".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.END;
                    return new ConsultationResponseDTO("Severe pain, particularly after an injury, warrants immediate medical attention. It's crucial to seek care at an emergency department or urgent care center to assess the extent of the injury and receive appropriate treatment. Severe pain can be a sign of a serious issue such as a fracture, internal injury, or other significant trauma that requires prompt evaluation by healthcare professionals. ", true);
                } else if ("inability".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.END;
                    return new ConsultationResponseDTO("While the restriction in movement may not be causing severe pain, it is still advisable to have it evaluated by a healthcare professional as soon as possible. An orthopedic specialist or your primary care physician can assess the extent of the injury and recommend appropriate treatment or further diagnostics. ", true);
                }
            }
            case SIX_QUESTION -> {
                if ("numbness".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.SEVEN_QUESTION;
                    return new ConsultationResponseDTO("Is the numbness 'Localized' to one specific area or 'Spreading' to different parts of your body?", false);
                } else if ("movement".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.SEVEN_QUESTION;
                    return new ConsultationResponseDTO("Is the continuous difficulty with movement accompanied by 'Pain' or 'Weakness'?", false);
                }
            }
            case SEVEN_QUESTION -> {
                if ("localized".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.END;
                    return new ConsultationResponseDTO("Localized numbness, especially if sudden, may require immediate medical attention to rule out nerve compression or other injuries. Please consider visiting an urgent care center or your healthcare provider for an evaluation.", true);
                } else if ("spreading".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.END;
                    return new ConsultationResponseDTO("Spreading numbness accompanied by other symptoms can be indicative of a more serious condition. Please seek immediate medical attention.", true);
                } else if ("pain".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.END;
                    return new ConsultationResponseDTO("If injury-related: \"Pain resulting from an injury, especially if severe or worsening, should be promptly evaluated by a healthcare professional. Visiting an emergency department or an urgent care center is advisable for acute or severe injuries. If unknown origin: \n Pain of unknown origin should not be ignored. It is important to consult with a healthcare provider for a thorough examination to determine the underlying cause and appropriate treatment.", true);
                } else if ("weakness".equalsIgnoreCase(userResponse)) {
                    currentStage = ConsultationStage.END;
                    return new ConsultationResponseDTO("If localized: \"Localized weakness, particularly if sudden or after an injury, can indicate a specific issue such as nerve compression or muscle injury. It is important to seek medical attention from a healthcare provider or a specialist such as a neurologist or orthopedic doctor. \n If general: General weakness can be a symptom of a wide range of conditions, from nutritional deficiencies to more serious systemic issues. It is recommended to see your primary care physician for a comprehensive evaluation to identify the cause and appropriate treatment.", true);
                }
            }
        }

        currentStage = ConsultationStage.START;
        return new ConsultationResponseDTO("Invalid response. Please start again.", true);
    }

    public void resetConversation() {
        currentStage = ConsultationStage.START;
    }
}
