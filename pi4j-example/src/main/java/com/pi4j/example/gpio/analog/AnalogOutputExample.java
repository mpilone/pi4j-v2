package com.pi4j.example.gpio.analog;

/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: EXAMPLE :: Sample Code
 * FILENAME      :  AnalogOutputExample.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.analog.AnalogChangeListener;
import com.pi4j.io.gpio.analog.AnalogOutput;
import com.pi4j.util.Console;

public class AnalogOutputExample {

    public static int ANALOG_OUTPUT_PIN = 4;
    public static int SHUTDOWN_ANALOG_VALUE = -1;

    public AnalogOutputExample() {
    }

    public static void main(String[] args) throws Exception {

        // create Pi4J console wrapper/helper
        // (This is a utility class to abstract some of the boilerplate stdin/stdout code)
        final var console = new Console();

        // print program title/header
        console.title("<-- The Pi4J Project -->", "Basic Analog Output Example");

        // allow for user to exit program using CTRL-C
        console.promptForExit();

        // initialize the Pi4J library
        var pi4j = Pi4J.initialize();

        // create an analog output instance using the default analog output provider
        var output = AnalogOutput.instance(ANALOG_OUTPUT_PIN);
        output.config().shutdownValue(SHUTDOWN_ANALOG_VALUE);

        // setup a analog output listener to listen for any state changes on the analog output
        output.addListener((AnalogChangeListener) event -> {
            System.out.print("ANALOG OUTPUT [");
            System.out.print(event.source().address());
            System.out.print("] VALUE CHANGE: ");
            System.out.println(event.value());
        });

        // lets invoke some changes on the analog output
        output.value(-101.6774)
              .value(34)
              .value(000999d)
              .value(45.79942);

        // lets read the analog output state
        System.out.print("CURRENT ANALOG OUTPUT VALUE IS [");
        System.out.println(output.value() + "]");

        // shutdown Pi4J
        Pi4J.terminate();
    }
}
