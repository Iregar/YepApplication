package es.yepwarriors.yepwarriors.constants;

public class Constantes {
    public static final class FileTypes {
        public final static String IMAGE = "image";
        public final static String VIDEO = "video";
    }
    public static final class CameraActions {
        public static final int TAKE_PHOTO = 0;
        public static final int MAKE_VIDEO = 1;
        public static final int CHOOSE_PHOTO = 2;
        public static final int CHOOSE_VIDEO = 3;
    }
    public static final class Users {
        public static final String FIELD_USERNAME = "username";
        public static final String FIELD_PASSWORD = "password";
        public static final String FIELD_EMAIL = "email";
        public static final int MAX_USERS = 1000;
        public static final String FRIENDS_RELATION = "friendsRelation";
    }
    public static final class ParseClasses {
        public static final class Messages {
            public static final String CLASS = "message";
            public static final String KEY_ID_SENDER = "idSender";
            public static final String KEY_SENDER_NAME = "senderName";
            public static final String KEY_FILE = "file";
            public static final String KEY_FILE_TYPE = "fileType";
            public static final String KEY_ID_RECIPIENTS = "idRecipients";
            public static final String KEY_CREATED_AT = "createdAt";
            public static final String KEY_USER_ID="userId";
        }
    }
}
