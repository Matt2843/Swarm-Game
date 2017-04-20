package com.swarmer.server;

import com.swarmer.server.units.ServerUnit;
import com.swarmer.shared.communication.Callable;
import com.swarmer.shared.communication.Message;

import java.io.IOException;
import java.net.Socket;

public class CoordinationUnitCallable extends Callable {
    public CoordinationUnitCallable(Message message) throws IOException {
        super(new Socket("127.0.0.1", ServerUnit.COORDINATE_UNIT_TCP_PORT), message);
    }
}
