package com.habitoplus.habitoplusback.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.habitoplus.habitoplusback.dto.HabitDTO;
import com.habitoplus.habitoplusback.dto.HabitUpdateDTO;
import com.habitoplus.habitoplusback.dto.NotificationDTO;
import com.habitoplus.habitoplusback.dto.RecommendationDTO;
import com.habitoplus.habitoplusback.enums.Priority;
import com.habitoplus.habitoplusback.event.HabitStatusUpdatedEvent;
import com.habitoplus.habitoplusback.exception.AccountNotFoundException;
import com.habitoplus.habitoplusback.exception.CategoryNotFoundException;
import com.habitoplus.habitoplusback.exception.GraphNotFoundException;
import com.habitoplus.habitoplusback.exception.PixelPostException;
import com.habitoplus.habitoplusback.exception.PixelaAccountNotFoundException;
import com.habitoplus.habitoplusback.exception.PixelaConnectionException;
import com.habitoplus.habitoplusback.exception.PixelaIOException;
import com.habitoplus.habitoplusback.exception.UserNotFoundException;
import com.habitoplus.habitoplusback.model.Account;
import com.habitoplus.habitoplusback.model.Category;
import com.habitoplus.habitoplusback.model.Graph;
import com.habitoplus.habitoplusback.model.Habit;
import com.habitoplus.habitoplusback.model.Notification;
import com.habitoplus.habitoplusback.model.Pixela;
import com.habitoplus.habitoplusback.model.Profile;
import com.habitoplus.habitoplusback.repository.AccountRepository;
import com.habitoplus.habitoplusback.repository.GraphRepository;
import com.habitoplus.habitoplusback.repository.HabitRepository;
import com.habitoplus.habitoplusback.repository.PixelaRepository;
import jakarta.transaction.Transactional;

@Service
public class HabitService {

    @Autowired
    private HabitRepository repo;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private PixelaRepository pixelaRepository;

    @Autowired
    private PixelaService pixelaService;

    @Autowired
    private GraphService graphService;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private GraphRepository graphRepository;

    @Value("${api.key.OpenIA}")
    private String keyAPI;

    @Autowired
    private RestTemplate restTemplate;

    public List<Habit> getAll() {
        return repo.findAll();
    }

    public HabitDTO getById(Integer idHabit) {
        Habit habit = repo.findById(idHabit).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Habit with ID " + idHabit + " not found"));
        return convertToDTO(habit);
    }

    public List<Habit> getHabitsByProfile(Integer idProfile) {
        return repo.findAllHabitsOfAProfile(idProfile);
    }

    // @Transactional
    // public void saveHabit(HabitDTO habitDTO) {
    // Habit habit = convertToEntity(habitDTO);
    // repo.save(habit);
    // if (!habit.getStatus()) {
    // NotificationDTO notification = new NotificationDTO();
    // notification.setMessage("Recuerda realizar tu Habito " +
    // habit.getHabit_name() + " created");
    // notification.setDateNotification(Date.valueOf(LocalDate.now().plusDays(1)));
    // notification.setIsRead(false);
    // notification.setType("INFO");
    // notification.setIdHabit(habit.getIdHabit());
    // notificationService.createNotification(notification);
    // }
    // }
    @Transactional
    public void save(HabitDTO habitDTO) throws UserNotFoundException {
        // Convierte HabitDTO a la entidad Habit y guarda en la base de datos
        Habit habit = convertToEntity(habitDTO);
        Habit savedHabit = repo.save(habit);

        // Crea una notificación si el hábito está inactivo
        if (!savedHabit.getStatus()) {
            NotificationDTO notification = new NotificationDTO();
            notification.setMessage("Recuerda realizar tu Hábito " + savedHabit.getHabit_name() + " creado");
            notification.setDateNotification(Date.valueOf(LocalDate.now().plusDays(1)));
            notification.setIsRead(false);
            notification.setType("INFO");
            notification.setIdHabit(savedHabit.getIdHabit());

            notificationService.createNotification(notification);
        }
       
    }

    @Transactional
    public void save(Integer idHabit, HabitUpdateDTO habitDTO) throws UserNotFoundException {
        Habit habit = repo.findById(idHabit).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Habit with ID " + idHabit + " not found"));
        Category category = categoryService.getById(habitDTO.getCategoryId());
        habit.setIdHabit(idHabit);
        habit.setHabit_name(habitDTO.getHabitName());
        habit.setDescription(habitDTO.getDescription());
        habit.setPriority(habitDTO.getPriority());
        habit.setCategory(category);

        if (habit.getStatus()) {
            List<Notification> notifications = notificationService.getNotificationsByHabit(habit);
            for (Notification notification : notifications) {
                notificationService.deleteNotification(notification.getIdNotification());
            }
        } else {
            NotificationDTO notification = new NotificationDTO();
            notification.setMessage("Recuerda realizar tu Habito " + habit.getHabit_name() + " created");
            notification.setDateNotification(Date.valueOf(LocalDate.now().plusDays(1)));
            notification.setIsRead(false);
            notification.setType("INFO");
            notification.setIdHabit(habit.getIdHabit());

            notificationService.createNotification(notification);
        }

        repo.save(habit);
    }

    @Transactional
    public void save(Integer idHabit, Boolean status) throws UserNotFoundException {
        Habit habit = repo.findById(idHabit).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Habit with ID " + idHabit + " not found"));
        habit.setStatus(status);

        if (status) {
            List<Notification> notifications = notificationService.getNotificationsByHabit(habit);
            for (Notification notification : notifications) {
                notificationService.deleteNotification(notification.getIdNotification());
            }

            Profile profile = habit.getProfile();
            Account account = accountRepository.findByProfile(profile)
                    .orElseThrow(() -> new AccountNotFoundException(
                            "Account not found for profile ID: " + profile.getIdProfile()));
            Pixela pixela = pixelaRepository.findByAccountId(account.getIdAccount())
                    .orElseThrow(() -> new PixelaAccountNotFoundException(
                            "Pixela account not found for account: " + account.getEmail()));

            String pixelaUsername = pixela.getUsername().split("@")[0];
            String pixelaToken = pixela.getToken();

            Graph graph = graphRepository.findByPixelaAndGraphId(pixela, "habit-" + habit.getIdHabit())
                    .orElseThrow(
                            () -> new GraphNotFoundException("Graph not found for habit: " + habit.getHabit_name()));

            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            System.out.println(
                    "Posting pixel to Pixela - Username: " + pixelaUsername + ", Graph ID: " + graph.getGraphId()
                            + ", Date: " + currentDate + ", Quantity: 1");

            boolean pixelPosted = graphService.postGraphValue(pixelaUsername, graph.getGraphId(), pixelaToken,
                    currentDate, "1");
            if (!pixelPosted) {
                throw new PixelPostException("Failed to post pixel for habit: " + habit.getHabit_name());
            }
        } else {
            NotificationDTO notification = new NotificationDTO();
            notification.setMessage("Recuerda realizar tu Habito " + habit.getHabit_name() + " created");
            notification.setDateNotification(Date.valueOf(LocalDate.now().plusDays(1)));
            notification.setIsRead(false);
            notification.setType("INFO");
            notification.setIdHabit(habit.getIdHabit());

            notificationService.createNotification(notification);
        }
        repo.save(habit);

        eventPublisher.publishEvent(new HabitStatusUpdatedEvent(this, habit.getProfile().getIdProfile()));
    }

    @Transactional
    public void delete(Integer idHabit) {
        Habit habit = repo.findById(idHabit).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Habit with ID " + idHabit + " not found"));
        if (habit != null) {
            repo.deleteById(idHabit);
        }
    }

    public List<Habit> getHabitsByCategory(Integer idProfile, Integer idCategory) {
        if (profileService.getProfileById(idProfile) == null) {
            throw new UserNotFoundException("");
        }
        if (categoryService.getById(idCategory) == null) {
            throw new CategoryNotFoundException("");
        }

        return repo.findHabitsByCategory(idProfile, idCategory);
    }

    public List<Habit> getHabitsByPriority(Integer idProfile, Priority priority) {
        if (profileService.getProfileById(idProfile) == null) {
            throw new UserNotFoundException("");
        }
        return repo.findHabitsByPriority(idProfile, priority);
    }

    public List<RecommendationDTO> getRecommendations(Integer idProfile) {
        Profile profile = profileService.getProfileById(idProfile);
        if (profile == null) {
            throw new UserNotFoundException("");
        }

        String prompt = "Generate a list of 3 specific habits that you would recommend, expressed in an array of json objects where the format of the habit objects is {\"habitName\": \"string\", \"description\": \"string\", } where the habit name should be concise phrases like 'Healthy Cooking', and the description should be like Cook a healthy meal and should not be longer than 100 characters, considering the following preferences and profile description:\n"
                + "Preferences: " + profile.getPreferences() + ".\n"
                + "Description: " + profile.getDescription() + ".\n"
                + "Make sure each habit is clear and easy to understand.";

        JsonObject body = new JsonObject();
        body.addProperty("model", "gpt-3.5-turbo");
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", prompt);
        JsonArray messagesArray = new JsonArray();
        messagesArray.add(message);
        body.add("messages", messagesArray);
        String jsonString = body.toString();

        String url = "https://api.openai.com/v1/chat/completions";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(keyAPI);

        HttpEntity<String> request = new HttpEntity<>(jsonString, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        JsonObject responseBody = new Gson().fromJson(response.getBody(), JsonObject.class);
        JsonArray choices = responseBody.getAsJsonArray("choices");

        List<RecommendationDTO> recommendations = new ArrayList<>();
        for (JsonElement choice : choices) {
            String content = choice.getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();
            JsonArray recommendationsArray = new JsonArray();
            try {
                recommendationsArray = JsonParser.parseString(content).getAsJsonArray();
                for (JsonElement recommendationElement : recommendationsArray) {
                    JsonObject recommendationObject = recommendationElement.getAsJsonObject();
                    String habitName = recommendationObject.get("habitName").getAsString();
                    String description = recommendationObject.get("description").getAsString();
                    recommendations.add(new RecommendationDTO(habitName, description));
                }
            } catch (JsonSyntaxException e) {
                // Maneja el error si no se puede parsear correctamente
                e.printStackTrace();
            }
        }

        return recommendations;
    }

    public List<Habit> getHabitsByProfile(Integer idProfile, int page, int pageSize) {
        PageRequest pageReq = PageRequest.of(page, pageSize);
        if (profileService.getProfileById(idProfile) == null) {
            throw new UserNotFoundException("");
        } else {
            Page<Habit> habitsPage = repo.findAllHabitsOfAProfile(idProfile, pageReq);
            return habitsPage.getContent();
        }
    }

    public HabitDTO convertToDTO(Habit habit) {
        return modelMapper.map(habit, HabitDTO.class);
    }

    public Habit convertToEntity(HabitDTO habitDTO) {
        Habit habit = new Habit();
        habit.setDescription(habitDTO.getDescription());
        habit.setStatus(false);
        habit.setPriority(habitDTO.getPriority());
        habit.setHabit_name(habitDTO.getHabit_name());
        Category category = categoryService.getById(habitDTO.getCategoryId());
        Profile profile = profileService.getProfileById(habitDTO.getProfileId());
        habit.setCategory(category);
        habit.setProfile(profile);
        return habit;
    }

}
