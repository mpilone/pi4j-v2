package com.pi4j.library.gpiod.internal;

import com.pi4j.library.gpiod.util.NativeLibraryLoader;

import java.util.Arrays;

/**
 * <p>GpioD interface.</p>
 *
 * @author Alexander Liggesmeyer (<a href="https://alexander.liggesmeyer.net/">https://alexander.liggesmeyer.net/</a>)
 * @version $Id: $Id
 */
public class GpioD {
    // C library: https://git.kernel.org/pub/scm/libs/libgpiod/libgpiod.git/tree/include/gpiod.h?h=v1.6.x

    private GpioD() {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libgpiod.so", "pi4j-gpiod");
        NativeLibraryLoader.load("libpi4j-gpiod.so", "pi4j-gpiod");
    }

    static GpioChip chipOpen(String path) {
        Long ptr = c_gpiod_chip_open(path);
        if (ptr == null)
            throw new GpioDException("c_gpiod_chip_open failed!");
        return new GpioChip(ptr);
    }

    private static native Long c_gpiod_chip_open(String path);

    static void chipClose(GpioChip chip) {
        c_gpiod_chip_close(chip.getCPointer());
    }

    private static native void c_gpiod_chip_close(long chipPtr);

    static String chipGetName(GpioChip chip) {
        return c_gpiod_chip_name(chip.getCPointer());
    }

    private static native String c_gpiod_chip_name(long chipPtr);

    static String chipGetLabel(GpioChip chip) {
        return c_gpiod_chip_label(chip.getCPointer());
    }

    private static native String c_gpiod_chip_label(long chipPtr);

    static int chipGetNumLines(GpioChip chip) {
        return c_gpiod_chip_num_lines(chip.getCPointer());
    }

    private static native int c_gpiod_chip_num_lines(long chipPtr);

    static GpioLine chipGetLine(GpioChip chip, int offset) {
        Long linePtr = c_gpiod_chip_get_line(chip.getCPointer(), offset);
        if (linePtr == null)
            throw new GpioDException("c_gpiod_chip_get_line failed!");
        return new GpioLine(linePtr);
    }

    private static native Long c_gpiod_chip_get_line(long chipPtr, int offset);

    static void chipGetLines(GpioChip chip, int[] offsets, GpioLineBulk lineBulk) {
        int result = c_gpiod_chip_get_lines(chip.getCPointer(), offsets, offsets.length, lineBulk.getCPointer());
        if (result < 0)
            throw new GpioDException("c_gpiod_chip_get_lines failed: " + result);
    }

    private static native int c_gpiod_chip_get_lines(long chipPtr, int[] offsets, int numOffsets, long lineBulkPtr);

    static void chipGetAllLines(GpioChip chip, GpioLineBulk lineBulk) {
        int result = c_gpiod_chip_get_all_lines(chip.getCPointer(), lineBulk.getCPointer());
        if (result < 0)
            throw new GpioDException("c_gpiod_chip_get_all_lines failed: " + result);
    }

    private static native int c_gpiod_chip_get_all_lines(long chipPtr, long lineBulkPtr);

    static GpioLine chipGetLine(GpioChip chip, String name) {
        Long linePtr = c_gpiod_chip_find_line(chip.getCPointer(), name);
        if (linePtr == null)
            throw new GpioDException("c_gpiod_chip_find_line failed!");
        return new GpioLine(linePtr);
    }

    private static native Long c_gpiod_chip_find_line(long chipPtr, String name);

    static void lineBulkFree(GpioLineBulk lineBulk) {
        c_gpiod_line_bulk_free(lineBulk.getCPointer());
    }

    private static native void c_gpiod_line_bulk_free(long lineBulkPtr);

    private static native void gpiod_line_bulk_init(long lineBulkPtr);

    static long lineBulkNew() {
        Long ptr = c_gpiod_line_bulk_new();
        if (ptr == null)
            throw new GpioDException("c_gpiod_line_bulk_new failed!");
        gpiod_line_bulk_init(ptr);
        return ptr;
    }

    private static native Long c_gpiod_line_bulk_new();

    static void lineBulkAdd(GpioLineBulk lineBulk, GpioLine line) {
        c_gpiod_line_bulk_add(lineBulk.getCPointer(), line.getCPointer());
    }

    private static native void c_gpiod_line_bulk_add(long lineBulkPtr, long linePtr);

    static GpioLine lineBulkGetLine(GpioLineBulk lineBulk, int offset) {
        return new GpioLine(c_gpiod_line_bulk_get_line(lineBulk.getCPointer(), offset));
    }

    private static native long c_gpiod_line_bulk_get_line(long lineBulkPtr, int offset);

    static int lineBulkGetNumLines(GpioLineBulk lineBulk) {
        return c_gpiod_line_bulk_num_lines(lineBulk.getCPointer());
    }

    private static native int c_gpiod_line_bulk_num_lines(long lineBulkPtr);

    public enum LINE_DIRECTION {
        INPUT(1),
        OUTPUT(2);
        final int val;

        public int getVal() {
            return val;
        }

        LINE_DIRECTION(int val) {
            this.val = val;
        }

        static LINE_DIRECTION fromInt(int val) {
            for (LINE_DIRECTION dir : LINE_DIRECTION.values()) {
                if (dir.val == val)
                    return dir;
            }
            throw new IllegalStateException("Unexpected LINE_DIRECTION value: " + val);
        }
    }

    public enum LINE_ACTIVE_STATE {
        HIGH(1),
        LOW(2);
        final int val;

        public int getVal() {
            return val;
        }

        LINE_ACTIVE_STATE(int val) {
            this.val = val;
        }

        static LINE_ACTIVE_STATE fromInt(int val) {
            for (LINE_ACTIVE_STATE dir : LINE_ACTIVE_STATE.values()) {
                if (dir.val == val)
                    return dir;
            }
            throw new IllegalStateException("Unexpected LINE_ACTIVE_STATE value: " + val);
        }
    }

    public enum LINE_BIAS {
        AS_IS(1),
        DISABLE(2),
        PULL_UP(3),
        PULL_DOWN(4);
        final int val;

        public int getVal() {
            return val;
        }

        LINE_BIAS(int val) {
            this.val = val;
        }

        static LINE_BIAS fromInt(int val) {
            for (LINE_BIAS dir : LINE_BIAS.values()) {
                if (dir.val == val)
                    return dir;
            }
            throw new IllegalStateException("Unexpected LINE_BIAS value: " + val);
        }
    }

    static int lineGetOffset(GpioLine line) {
        return c_gpiod_line_offset(line.getCPointer());
    }

    private static native int c_gpiod_line_offset(long linePtr);

    static String lineGetName(GpioLine line) {
        return c_gpiod_line_name(line.getCPointer());
    }

    private static native String c_gpiod_line_name(long linePtr);

    static String lineGetConsumer(GpioLine line) {
        return gpiod_line_consumer(line.getCPointer());
    }

    private static native String gpiod_line_consumer(long linePtr);

    static LINE_DIRECTION lineGetDirection(GpioLine line) {
        return LINE_DIRECTION.fromInt(c_gpiod_line_direction(line.getCPointer()));
    }

    private static native int c_gpiod_line_direction(long linePtr);

    static LINE_ACTIVE_STATE lineGetActiveState(GpioLine line) {
        return LINE_ACTIVE_STATE.fromInt(c_gpiod_line_active_state(line.getCPointer()));
    }

    private static native int c_gpiod_line_active_state(long linePtr);

    static LINE_BIAS lineGetBias(GpioLine line) {
        return LINE_BIAS.fromInt(c_gpiod_line_bias(line.getCPointer()));
    }

    private static native int c_gpiod_line_bias(long linePtr);

    static boolean lineIsUsed(GpioLine line) {
        return c_gpiod_line_is_used(line.getCPointer());
    }

    private static native boolean c_gpiod_line_is_used(long linePtr);

    static boolean lineIsOpenDrain(GpioLine line) {
        return c_gpiod_line_is_open_drain(line.getCPointer());
    }

    private static native boolean c_gpiod_line_is_open_drain(long linePtr);

    static boolean lineIsOpenSource(GpioLine line) {
        return c_gpiod_line_is_open_source(line.getCPointer());
    }

    private static native boolean c_gpiod_line_is_open_source(long linePtr);

    static void lineUpdate(GpioLine line) {
        int result = c_gpiod_line_update(line.getCPointer());
        if (result < 0)
            throw new GpioDException("c_gpiod_line_update failed: " + result);
    }

    private static native int c_gpiod_line_update(long linePtr);

    public enum LINE_REQUEST {
        DIRECTION_AS_IS(1),
        DIRECTION_INPUT(2),
        DIRECTION_OUTPUT(3),
        EVENT_FALLING_EDGE(4),
        EVENT_RISING_EDGE(5),
        EVENT_BOTH_EDGES(6);
        final int val;

        public int getVal() {
            return val;
        }

        LINE_REQUEST(int val) {
            this.val = val;
        }

        static LINE_REQUEST fromInt(int val) {
            for (LINE_REQUEST dir : LINE_REQUEST.values()) {
                if (dir.val == val)
                    return dir;
            }
            throw new IllegalStateException("Unexpected LINE_REQUEST value: " + val);
        }
    }

    public enum LINE_REQUEST_FLAG {
        OPEN_DRAIN((byte) 1),
        OPEN_SOURCE((byte) (1 << 1)),
        ACTIVE_LOW((byte) (1 << 2)),
        BIAS_DISABLE((byte) (1 << 3)),
        BIAS_PULL_DOWN((byte) (1 << 4)),
        BIAS_PULL_UP((byte) (1 << 5));
        final byte val;

        LINE_REQUEST_FLAG(byte val) {
            this.val = val;
        }

        public byte getVal() {
            return val;
        }

        static LINE_REQUEST_FLAG fromByte(byte val) {
            for (LINE_REQUEST_FLAG dir : LINE_REQUEST_FLAG.values()) {
                if (dir.val == val)
                    return dir;
            }
            throw new IllegalStateException("Unexpected LINE_REQUEST_FLAG value: " + val);
        }
    }

    static void lineRequest(GpioLine line, GpioLineRequest request, int defaultVal) {
        int result = c_gpiod_line_request(line.getCPointer(), request.getCPointer(), defaultVal);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_request failed: " + result);
    }

    private static native int c_gpiod_line_request(long linePtr, long lineRequestPtr, int default_val);

    static void lineRequestInput(GpioLine line, String consumer) {
        int result = c_gpiod_line_request_input(line.getCPointer(), consumer);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_request_input failed: " + result);
    }

    private static native int c_gpiod_line_request_input(long linePtr, String consumer);

    static void lineRequestOutput(GpioLine line, String consumer, int defaultVal) {
        int result = c_gpiod_line_request_output(line.getCPointer(), consumer, defaultVal);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_request_output failed: " + result);
    }

    private static native int c_gpiod_line_request_output(long linePtr, String consumer, int default_val);

    static void lineRequestRisingEdgeEvents(GpioLine line, String consumer) {
        int result = c_gpiod_line_request_rising_edge_events(line.getCPointer(), consumer);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_request_rising_edge_events failed: " + result);
    }

    private static native int c_gpiod_line_request_rising_edge_events(long linePtr, String consumer);

    static void lineRequestFallingEdgeEvents(GpioLine line, String consumer) {
        int result = c_gpiod_line_request_falling_edge_events(line.getCPointer(), consumer);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_request_falling_edge_events failed: " + result);
    }

    private static native int c_gpiod_line_request_falling_edge_events(long linePtr, String consumer);

    static void lineRequestBothEdgeEvents(GpioLine line, String consumer) {
        int result = c_gpiod_line_request_both_edges_events(line.getCPointer(), consumer);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_request_both_edges_events failed: " + result);
    }

    private static native int c_gpiod_line_request_both_edges_events(long linePtr, String consumer);

    static void lineRequestInputFlags(GpioLine line, String consumer, int flags) {
        int result = c_gpiod_line_request_input_flags(line.getCPointer(), consumer, flags);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_request_input_flags failed: " + result);
    }

    private static native int c_gpiod_line_request_input_flags(long linePtr, String consumer, int flags);

    static void lineRequestOutputFlags(GpioLine line, String consumer, int flags, int defaultVal) {
        int result = c_gpiod_line_request_output_flags(line.getCPointer(), consumer, flags, defaultVal);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_request_output_flags failed: " + result);
    }

    private static native int c_gpiod_line_request_output_flags(long linePtr, String consumer, int flags,
        int default_val);

    static void lineRequestRisingEdgeEventsFlags(GpioLine line, String consumer, int flags) {
        int result = c_gpiod_line_request_rising_edge_events_flags(line.getCPointer(), consumer, flags);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_request_rising_edge_events_flags failed: " + result);
    }

    private static native int c_gpiod_line_request_rising_edge_events_flags(long linePtr, String consumer, int flags);

    static void lineRequestFallingEdgeEventsFlags(GpioLine line, String consumer, int flags) {
        int result = c_gpiod_line_request_falling_edge_events_flags(line.getCPointer(), consumer, flags);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_request_falling_edge_events_flags failed: " + result);
    }

    private static native int c_gpiod_line_request_falling_edge_events_flags(long linePtr, String consumer, int flags);

    static void lineRequestBothEdgeEventsFlags(GpioLine line, String consumer, int flags) {
        int result = c_gpiod_line_request_both_edges_events_flags(line.getCPointer(), consumer, flags);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_request_both_edges_events_flags failed: " + result);
    }

    private static native int c_gpiod_line_request_both_edges_events_flags(long linePtr, String consumer, int flags);

    static void lineRequestBulk(GpioLineBulk lineBulk, GpioLineRequest lineRequest, int[] defaultVals) {
        int result = c_gpiod_line_request_bulk(lineBulk.getCPointer(), lineRequest.getCPointer(), defaultVals);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_request_bulk failed: " + result);
    }

    private static native int c_gpiod_line_request_bulk(long lineBulkPtr, long lineRequestPtr, int[] default_vals);

    static void lineRequestBulkInput(GpioLineBulk lineBulk, String consumer) {
        int result = c_gpiod_line_request_bulk_input(lineBulk.getCPointer(), consumer);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_request_bulk_input failed: " + result);
    }

    private static native int c_gpiod_line_request_bulk_input(long lineBulkPtr, String consumer);

    static void lineRequestBulkOutput(GpioLineBulk lineBulk, String consumer, int[] defaultVals) {
        int result = c_gpiod_line_request_bulk_output(lineBulk.getCPointer(), consumer, defaultVals);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_request_bulk_output failed: " + result);
    }

    private static native int c_gpiod_line_request_bulk_output(long lineBulkPtr, String consumer, int[] default_vals);

    static void lineRequestBulkRisingEdgeEvents(GpioLineBulk lineBulk, String consumer) {
        int result = c_gpiod_line_request_bulk_rising_edge_events(lineBulk.getCPointer(), consumer);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_request_bulk_rising_edge_events failed: " + result);
    }

    private static native int c_gpiod_line_request_bulk_rising_edge_events(long lineBulkPtr, String consumer);

    static void lineRequestBulkFallingEdgeEvents(GpioLineBulk lineBulk, String consumer) {
        int result = c_gpiod_line_request_bulk_falling_edge_events(lineBulk.getCPointer(), consumer);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_request_bulk_falling_edge_events failed: " + result);
    }

    private static native int c_gpiod_line_request_bulk_falling_edge_events(long lineBulkPtr, String consumer);

    static void lineRequestBulkBothEdgeEvents(GpioLineBulk lineBulk, String consumer) {
        int result = c_gpiod_line_request_bulk_both_edges_events(lineBulk.getCPointer(), consumer);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_request_bulk_both_edges_events failed: " + result);
    }

    private static native int c_gpiod_line_request_bulk_both_edges_events(long lineBulkPtr, String consumer);

    static void lineRequestBulkInputFlags(GpioLineBulk lineBulk, String consumer, int flags) {
        int result = c_gpiod_line_request_bulk_input_flags(lineBulk.getCPointer(), consumer, flags);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_request_bulk_input_flags failed: " + result);
    }

    private static native int c_gpiod_line_request_bulk_input_flags(long lineBulkPtr, String consumer, int flags);

    static void lineRequestBulkOutputFlags(GpioLineBulk lineBulk, String consumer, int flags, int[] defaultVals) {
        int result = c_gpiod_line_request_bulk_output_flags(lineBulk.getCPointer(), consumer, flags, defaultVals);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_request_bulk_output_flags failed: " + result);
    }

    private static native int c_gpiod_line_request_bulk_output_flags(long lineBulkPtr, String consumer, int flags,
        int[] default_vals);

    static void lineRequestBulkRisingEdgeEventFlags(GpioLineBulk lineBulk, String consumer, int flags) {
        int result = c_gpiod_line_request_bulk_rising_edge_events_flags(lineBulk.getCPointer(), consumer, flags);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_request_bulk_rising_edge_events_flags failed: " + result);
    }

    private static native int c_gpiod_line_request_bulk_rising_edge_events_flags(long lineBulkPtr, String consumer,
        int flags);

    static void lineRequestBulkFallingEdgeEventFlags(GpioLineBulk lineBulk, String consumer, int flags) {
        int result = c_gpiod_line_request_bulk_falling_edge_events_flags(lineBulk.getCPointer(), consumer, flags);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_request_bulk_falling_edge_events_flags failed: " + result);
    }

    private static native int c_gpiod_line_request_bulk_falling_edge_events_flags(long lineBulkPtr, String consumer,
        int flags);

    static void lineRequestBulkBothEdgeEventFlags(GpioLineBulk lineBulk, String consumer, int flags) {
        int result = c_gpiod_line_request_bulk_both_edges_events_flags(lineBulk.getCPointer(), consumer, flags);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_request_bulk_both_edges_events_flags failed: " + result);
    }

    private static native int c_gpiod_line_request_bulk_both_edges_events_flags(long lineBulkPtr, String consumer,
        int flags);

    static void lineRelease(GpioLine line) {
        c_gpiod_line_release(line.getCPointer());
    }

    private static native void c_gpiod_line_release(long linePtr);

    static void lineBulkRelease(GpioLineBulk lineBulk) {
        c_gpiod_line_release_bulk(lineBulk.getCPointer());
    }

    private static native void c_gpiod_line_release_bulk(long lineBulkPtr);

    static boolean lineIsRequested(GpioLine line) {
        return c_gpiod_line_is_requested(line.getCPointer());
    }

    private static native boolean c_gpiod_line_is_requested(long linePtr);

    static boolean lineIsFree(GpioLine line) {
        return c_gpiod_line_is_free(line.getCPointer());
    }

    private static native boolean c_gpiod_line_is_free(long linePtr);

    static int lineGetValue(GpioLine line) {
        int result = c_gpiod_line_get_value(line.getCPointer());
        if (result < 0)
            throw new GpioDException("c_gpiod_line_get_value failed: " + result);
        return result;
    }

    private static native int c_gpiod_line_get_value(long linePtr);

    static int[] lineBulkGetValues(GpioLineBulk lineBulk) {
        int numValues = lineBulkGetNumLines(lineBulk);
        int[] values = new int[numValues];
        int result = c_gpiod_line_get_value_bulk(lineBulk.getCPointer(), values);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_get_value_bulk failed: " + result);
        return values;
    }

    private static native int c_gpiod_line_get_value_bulk(long lineBulkPtr, int[] values);

    static void lineSetValue(GpioLine line, int value) {
        int result = c_gpiod_line_set_value(line.getCPointer(), value);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_set_value failed: " + result);
    }

    private static native int c_gpiod_line_set_value(long linePtr, int value);

    static void lineBulkSetValue(GpioLineBulk lineBulk, int[] values) {
        int result = c_gpiod_line_set_value_bulk(lineBulk.getCPointer(), values);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_set_value_bulk failed: " + result);
    }

    private static native int c_gpiod_line_set_value_bulk(long lineBulkPtr, int[] values);

    static void lineSetConfig(GpioLine line, LINE_REQUEST direction, int flags, int value) {
        int result = c_gpiod_line_set_config(line.getCPointer(), direction.val, flags, value);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_set_config failed: " + result);
    }

    private static native int c_gpiod_line_set_config(long linePtr, int direction, int flags, int value);

    static void lineBulkSetConfig(GpioLineBulk lineBulk, LINE_REQUEST direction, int flags, int[] values) {
        int result = c_gpiod_line_set_config_bulk(lineBulk.getCPointer(), direction.val, flags, values);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_set_config_bulk failed: " + result);
    }

    private static native int c_gpiod_line_set_config_bulk(long lineBulkPtr, int direction, int flags, int[] values);

    static void lineSetFlags(GpioLine line, int flags) {
        int result = c_gpiod_line_set_flags(line.getCPointer(), flags);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_set_flags failed: " + result);
    }

    private static native int c_gpiod_line_set_flags(long linePtr, int flags);

    static void lineBulkSetFlags(GpioLineBulk lineBulk, int flags) {
        int result = c_gpiod_line_set_flags_bulk(lineBulk.getCPointer(), flags);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_set_flags_bulk failed: " + result);
    }

    private static native int c_gpiod_line_set_flags_bulk(long lineBulkPtr, int flags);

    static void lineSetDirectionInput(GpioLine line) {
        int result = c_gpiod_line_set_direction_input(line.getCPointer());
        if (result < 0)
            throw new GpioDException("c_gpiod_line_set_direction_input failed: " + result);
    }

    private static native int c_gpiod_line_set_direction_input(long linePtr);

    static void lineSetDirectionOutputBulk(GpioLineBulk lineBulk) {
        int result = c_gpiod_line_set_direction_input_bulk(lineBulk.getCPointer());
        if (result < 0)
            throw new GpioDException("c_gpiod_line_set_direction_input_bulk failed: " + result);
    }

    private static native int c_gpiod_line_set_direction_input_bulk(long lineBulkPtr);

    static void lineSetDirectionOutput(GpioLine line, int value) {
        int result = c_gpiod_line_set_direction_output(line.getCPointer(), value);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_set_direction_output failed: " + result);
    }

    private static native int c_gpiod_line_set_direction_output(long linePtr, int value);

    static void lineSetDirectionOutputBulk(GpioLineBulk lineBulk, int[] values) {
        int result = c_gpiod_line_set_direction_output_bulk(lineBulk.getCPointer(), values);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_set_direction_output_bulk failed: " + result);
    }

    private static native int c_gpiod_line_set_direction_output_bulk(long lineBulkPtr, int[] values);

    public enum LINE_EVENT {
        RISING_EDGE(1),
        FALLING_EDGE(2);
        final int val;

        public int getVal() {
            return val;
        }

        LINE_EVENT(int val) {
            this.val = val;
        }

        static LINE_EVENT fromInt(int val) {
            for (LINE_EVENT dir : LINE_EVENT.values()) {
                if (dir.val == val)
                    return dir;
            }
            throw new IllegalStateException("Unexpected LINE_EVENT value: " + val);
        }
    }

    static boolean lineEventWait(GpioLine line, long timeoutNs) {
        int result = c_gpiod_line_event_wait(line.getCPointer(), timeoutNs);
        if (result < 0)
            throw new GpioDException("c_gpiod_line_event_wait failed: " + result);
        return result > 0;
    }

    private static native int c_gpiod_line_event_wait(long linePtr, long timeoutNs);

    static boolean lineBulkEventWait(GpioLineBulk lineBulk, long timeoutNs, GpioLineBulk eventBulk) {
        int result = c_gpiod_line_event_wait_bulk(lineBulk.getCPointer(), timeoutNs, eventBulk.getCPointer());
        if (result < 0)
            throw new GpioDException("c_gpiod_line_event_wait_bulk failed: " + result);
        return result > 0;
    }

    private static native int c_gpiod_line_event_wait_bulk(long lineBulkPtr, long timeoutNs, long eventBulkPtr);

    static boolean lineEventRead(GpioLine line, GpioLineEvent event) {
        int result = c_gpiod_line_event_read(line.getCPointer(), event.getCPointer());
        if (result < 0)
            throw new GpioDException("c_gpiod_line_event_read failed: " + result);
        return result > 0;
    }

    private static native int c_gpiod_line_event_read(long linePtr, long eventPtr);

    static GpioLineEvent[] lineEventReadMultiple(GpioLine line, int maxRead) {
        GpioLineEvent[] events = new GpioLineEvent[maxRead];
        for (int i = 0; i < events.length; i++) {
            events[i] = new GpioLineEvent();
        }

        int numRead = c_gpiod_line_event_read_multiple(line.getCPointer(),
            Arrays.stream(events).mapToLong(GpioLineEvent::getCPointer).toArray(), events.length);
        if (numRead < 0)
            throw new GpioDException("c_gpiod_line_event_read_multiple failed: " + numRead);

        GpioLineEvent[] result = new GpioLineEvent[numRead];
        if (numRead == maxRead) {
            result = events;
        } else {
            System.arraycopy(events, 0, result, 0, result.length);
        }
        return result;
    }

    private static native int c_gpiod_line_event_read_multiple(long linePtr, long[] eventPtr, int num_events);

    static GpioLine lineGet(String device, int offset) {
        Long ptr = c_gpiod_line_get(device, offset);
        if (ptr == null)
            throw new GpioDException("c_gpiod_line_get failed!");
        return new GpioLine(ptr);
    }

    private static native Long c_gpiod_line_get(String device, int offset);

    static GpioLine lineFind(String name) {
        Long ptr = gpiod_line_find(name);
        if (ptr == null)
            throw new GpioDException("gpiod_line_find failed!");
        return new GpioLine(ptr);
    }

    private static native Long gpiod_line_find(String name);

    static void lineCloseChip(GpioLine line) {
        c_gpiod_line_close_chip(line.getCPointer());
    }

    private static native void c_gpiod_line_close_chip(long linePtr);

    static GpioChip lineGetChip(GpioLine line) {
        return new GpioChip(gpiod_line_get_chip(line.getCPointer()));
    }

    private static native long gpiod_line_get_chip(long linePtr);

    static long chipIterNew() {
        Long ptr = gpiod_chip_iter_new();
        if (ptr == null)
            throw new GpioDException("gpiod_chip_iter_new failed!");
        return ptr;
    }

    private static native Long gpiod_chip_iter_new();

    static void chipIterFree(GpioChipIterator iter) {
        c_gpiod_chip_iter_free(iter.getCPointer());
    }

    private static native void c_gpiod_chip_iter_free(long chipIterPtr);

    static void chipIterFreeNoClose(GpioChipIterator iter) {
        c_gpiod_chip_iter_free_noclose(iter.getCPointer());
    }

    private static native void c_gpiod_chip_iter_free_noclose(long chipIterPtr);

    static GpioChip chipIterNext(GpioChipIterator iter) {
        Long ptr = c_gpiod_chip_iter_next(iter.getCPointer());
        if (ptr == null)
            return null;
        return new GpioChip(ptr);
    }

    private static native Long c_gpiod_chip_iter_next(long chipIterPtr);

    static GpioChip chipIterNextNoClose(GpioChipIterator iter) {
        Long ptr = c_gpiod_chip_iter_next_noclose(iter.getCPointer());
        if (ptr == null)
            return null;
        return new GpioChip(ptr);
    }

    private static native Long c_gpiod_chip_iter_next_noclose(long chipIterPtr);

    static long lineIterNew(GpioChip chip) {
        Long ptr = gpiod_line_iter_new(chip.getCPointer());
        if (ptr == null)
            throw new GpioDException("gpiod_line_iter_new failed!");
        return ptr;
    }

    private static native Long gpiod_line_iter_new(long chipPtr);

    static void lineIterFree(GpioLineIterator iter) {
        gpiod_line_iter_free(iter.getCPointer());
    }

    private static native void gpiod_line_iter_free(long lineIterPtr);

    static GpioLine lineIterNext(GpioLineIterator iter) {
        Long ptr = gpiod_line_iter_next(iter.getCPointer());
        if (ptr == null)
            return null;
        return new GpioLine(ptr);
    }

    private static native Long gpiod_line_iter_next(long lineIterPtr);

    static long lineEventGetTimespec(GpioLineEvent event) {
        return c_gpiod_line_event_get_timespec(event.getCPointer());
    }

    private static native long c_gpiod_line_event_get_timespec(long lineEventPtr);

    static LINE_EVENT lineEventGetType(GpioLineEvent event) {
        return LINE_EVENT.fromInt(c_gpiod_line_event_get_type(event.getCPointer()));
    }

    private static native int c_gpiod_line_event_get_type(long lineEventPtr);

    static long lineEventNew() {
        Long ptr = c_gpiod_line_event_new();
        if (ptr == null)
            throw new GpioDException("c_gpiod_line_event_new failed!");
        return ptr;
    }

    private static native Long c_gpiod_line_event_new();

    static void lineEventFree(GpioLineEvent event) {
        c_gpiod_line_event_free(event.getCPointer());
    }

    private static native void c_gpiod_line_event_free(long eventPtr);

    static String getVersion() {
        return c_gpiod_version_string();
    }

    private static native String c_gpiod_version_string();
}