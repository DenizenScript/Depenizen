package com.denizenscript.depenizen.bukkit.clientizen.events;

import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.depenizen.bukkit.clientizen.DataDeserializer;
import com.denizenscript.depenizen.bukkit.clientizen.DataSerializer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayerPressReleaseKeyClientizenEvent extends ClientizenScriptEvent {

    private static final Set<Integer> listenToPressKeys = new HashSet<>();
    private static final Set<Integer> listenToReleaseKeys = new HashSet<>();

    public KeyboardKeys key;
    public boolean pressed;

    public PlayerPressReleaseKeyClientizenEvent() {
        registerCouldMatcher("clientizen player presses|releases <'key'>");
        id = "PlayerPressReleaseKey";
    }

    @Override
    public boolean matches(ScriptPath path) {
        String keyMatcher = path.eventArgLowerAt(3);
        if (!keyMatcher.equals("key") && !runGenericCheck(keyMatcher, key.getName())) {
            return false;
        }
        if (pressed && !path.eventArgLowerAt(2).equals("presses")) {
            return false;
        }
        return super.matches(path);
    }

    @Override
    public ObjectTag getContext(String name) {
        switch (name) {
            case "key": return new ElementTag(key.getName());
        }
        return super.getContext(name);
    }

    @Override
    public void init() {
        for (ScriptPath path : eventPaths) {
            String keyMatcher = path.eventArgLowerAt(3);
            if (path.eventArgLowerAt(2).equals("presses")) {
                listenToPressKeys.addAll(KeyboardKeys.getKeyCodesMatching(keyMatcher));
            }
            else {
                listenToReleaseKeys.addAll(KeyboardKeys.getKeyCodesMatching(keyMatcher));
            }
        }
        super.init();
    }

    @Override
    public void destroy() {
        listenToPressKeys.clear();
        listenToReleaseKeys.clear();
        super.destroy();
    }

    @Override
    public void fire(DataDeserializer data) {
        key = KeyboardKeys.keysByCode.get(data.readInt());
        pressed = data.readBoolean();
        fire();
    }

    @Override
    public void write(DataSerializer serializer) {
        serializer.writeIntList(listenToPressKeys);
        serializer.writeIntList(listenToReleaseKeys);
    }
    
    enum KeyboardKeys {
        UNKNOWN(-1),
        SPACE(32),
        APOSTROPHE(39),
        COMMA(44),
        MINUS(45),
        PERIOD(46),
        SLASH(47),
        KEY_0(48, "0"),
        KEY_1(49, "1"),
        KEY_2(50, "2"),
        KEY_3(51, "3"),
        KEY_4(52, "4"),
        KEY_5(53, "5"),
        KEY_6(54, "6"),
        KEY_7(55, "7"),
        KEY_8(56, "8"),
        KEY_9(57, "9"),
        SEMICOLON(59),
        EQUAL(61),
        A(65),
        B(66),
        C(67),
        D(68),
        E(69),
        F(70),
        G(71),
        H(72),
        I(73),
        J(74),
        K(75),
        L(76),
        M(77),
        N(78),
        O(79),
        P(80),
        Q(81),
        R(82),
        S(83),
        T(84),
        U(85),
        V(86),
        W(87),
        X(88),
        Y(89),
        Z(90),
        LEFT_BRACKET(91),
        BACKSLASH(92),
        RIGHT_BRACKET(93),
        GRAVE_ACCENT(96),
        WORLD_1(161),
        WORLD_2(162),
        ESCAPE(256),
        ENTER(257),
        TAB(258),
        BACKSPACE(259),
        INSERT(260),
        DELETE(261),
        ARROW_RIGHT(262),
        ARROW_LEFT(263),
        ARROW_DOWN(264),
        ARROW_UP(265),
        PAGE_UP(266),
        PAGE_DOWN(267),
        HOME(268),
        END(269),
        CAPS_LOCK(280),
        SCROLL_LOCK(281),
        NUM_LOCK(282),
        PRINT_SCREEN(283),
        PAUSE(284),
        F1(290),
        F2(291),
        F3(292),
        F4(293),
        F5(294),
        F6(295),
        F7(296),
        F8(297),
        F9(298),
        F10(299),
        F11(300),
        F12(301),
        F13(302),
        F14(303),
        F15(304),
        F16(305),
        F17(306),
        F18(307),
        F19(308),
        F20(309),
        F21(310),
        F22(311),
        F23(312),
        F24(313),
        F25(314),
        KP_0(320),
        KP_1(321),
        KP_2(322),
        KP_3(323),
        KP_4(324),
        KP_5(325),
        KP_6(326),
        KP_7(327),
        KP_8(328),
        KP_9(329),
        KP_DECIMAL(330),
        KP_DIVIDE(331),
        KP_MULTIPLY(332),
        KP_SUBTRACT(333),
        KP_ADD(334),
        KP_ENTER(335),
        KP_EQUAL(336),
        LEFT_SHIFT(340),
        LEFT_CONTROL(341),
        LEFT_ALT(342),
        LEFT_SUPER(343),
        RIGHT_SHIFT(344),
        RIGHT_CONTROL(345),
        RIGHT_ALT(346),
        RIGHT_SUPER(347),
        MENU(348);
        
        public final int code;
        public String alternateName;
        
        KeyboardKeys(int code) {
            this.code = code;
        }

        KeyboardKeys(int code, String alternateName) {
            this.code = code;
            this.alternateName = alternateName;
        }

        public String getName() {
            return alternateName == null ? name() : alternateName;
        }

        public static Set<Integer> getKeyCodesMatching(String matcher) {
            Set<Integer> result = new HashSet<>();
            if (matcher.equals("key")) {
                result.addAll(keysByCode.keySet());
                return result;
            }
            MatchHelper matchHelper = ScriptEvent.createMatcher(matcher);
            for (KeyboardKeys key : KeyboardKeys.values()) {
                if (matchHelper.doesMatch(key.getName())) {
                    result.add(key.code);
                }
            }
            return result;
        }

        public static final Map<Integer, KeyboardKeys> keysByCode = Arrays.stream(KeyboardKeys.values()).collect(Collectors.toMap(key -> key.code, key -> key));
    }
}
