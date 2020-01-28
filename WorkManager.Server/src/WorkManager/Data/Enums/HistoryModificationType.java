package WorkManager.Data.Enums;

import java.util.HashMap;
import java.util.Map;

public enum HistoryModificationType
{
        Insert((byte)0),
        Update((byte)1),
        Delete((byte)2);

        private byte value;
        private static Map map = new HashMap<>();

        private HistoryModificationType(byte value) {
                this.value = value;
        }

        static {
                for (HistoryModificationType pageType : HistoryModificationType.values()) {
                        map.put(pageType.value, pageType);
                }
        }

        public static HistoryModificationType valueOf(byte pageType) {
                return (HistoryModificationType) map.get(pageType);
        }

        public int getValue() {
                return value;
        }
}
