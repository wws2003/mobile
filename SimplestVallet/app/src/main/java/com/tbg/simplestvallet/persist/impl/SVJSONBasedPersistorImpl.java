package com.tbg.simplestvallet.persist.impl;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.JsonWriter;

import com.tbg.simplestvallet.app.SimplestValetApp;
import com.tbg.simplestvallet.persist.abstr.ISVPersistable;
import com.tbg.simplestvallet.persist.abstr.ISVPersistor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wws2003 on 4/16/16.
 */
public class SVJSONBasedPersistorImpl implements ISVPersistor {

    private static final String JSON_KEY_PACKET_IDENTIFIER = "0_packer_identifier";
    private static final String JSON_KEY_PAYLOAD_IDENTIFIER = "1_packer_payload";

    @Override
    public void persist(ISVPersistable persistable) throws SVPersistException {
        String saveFilePath = getSaveName(persistable);
        savePersistablePackets(persistable.getIterable().iterator(), saveFilePath);
    }

    @Override
    public void load(ISVPersistable persistable) throws SVLoadException {
        String savedFilePath = getSaveName(persistable);
        List<ISVPersistable.PersistPacket> packets = new ArrayList<>();
        loadPersistedPackets(packets, savedFilePath);
        persistable.loadFromIterable(packets);
    }

    @Override
    public void delete(ISVPersistable persistable) throws SVDeleteException {
        String saveFilePath = getSaveName(persistable);
        deletePersistableFile(saveFilePath);
    }

    private String getSaveName(ISVPersistable persistable) {
        String persistableIdentifier = persistable.getIdentifier();
        return persistableIdentifier;
    }

    //-------------------------------MARK: For writing-------------------------------
    private void savePersistablePackets(Iterator<ISVPersistable.PersistPacket> packetIterator, String saveFilePath) throws SVPersistException{
        JsonWriter writer = null;
        try {
            OutputStream outputStream = new BufferedOutputStream(SimplestValetApp.getContext().openFileOutput(saveFilePath, Context.MODE_PRIVATE));
            writer = new JsonWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writePersistablePackets(writer, packetIterator);
        }
        catch (Exception e) {
            throw new SVPersistException(e);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                throw new SVPersistException(e);
            }
        }
    }

    private void writePersistablePackets(JsonWriter writer, Iterator<ISVPersistable.PersistPacket> packetIterator) throws IOException {
        writer.beginArray();
        while (packetIterator.hasNext()) {
            ISVPersistable.PersistPacket packet = packetIterator.next();
            writePersistablePacket(writer, packet);
        }
        writer.endArray();
    }

    private void writePersistablePacket(JsonWriter writer, ISVPersistable.PersistPacket packet) throws IOException{
        writer.beginObject();
        //Packet identifier
        writer.name(JSON_KEY_PACKET_IDENTIFIER).value(packet.getIdentifier());

        //Packet payload
        writer.name(JSON_KEY_PAYLOAD_IDENTIFIER);
        writer.beginArray();
        for (String data : packet.getPayload()) {
            writer.value(data);
        }
        writer.endArray();

        writer.endObject();
    }

    //-------------------------------MARK: For reading-------------------------------
    private void loadPersistedPackets(List<ISVPersistable.PersistPacket> packets, String savedFilePath) throws SVLoadException {
        JsonReader reader = null;
        try {
            InputStream inputStream = new BufferedInputStream(SimplestValetApp.getContext().openFileInput(savedFilePath));
            reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
            readPersistedPackets(reader, packets);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new SVLoadException(e);
        }
        finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                throw new SVLoadException(e);
            }
        }
    }

    private void readPersistedPackets(JsonReader reader, List<ISVPersistable.PersistPacket> packets) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            ISVPersistable.PersistPacket packet = readPersistedPacket(reader);
            packets.add(packet);
        }
        reader.endArray();
    }

    private ISVPersistable.PersistPacket readPersistedPacket(JsonReader reader) throws IOException {
        String packetIdentifier = null;
        List<String> packetData = new ArrayList<>();

        reader.beginObject();

        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(JSON_KEY_PACKET_IDENTIFIER)) {
                //Packet identifier
                packetIdentifier = reader.nextString();
            }
            if (name.equals(JSON_KEY_PAYLOAD_IDENTIFIER) && reader.peek() != JsonToken.NULL) {
                //Packet payload
                reader.beginArray();

                while (reader.hasNext()) {
                    String data = reader.nextString();
                    packetData.add(data);
                }

                reader.endArray();
            }
        }

        reader.endObject();

        String[] packetPayload = new String[packetData.size()];
        packetData.toArray(packetPayload);

        ISVPersistable.PersistPacket packet = new ISVPersistable.PersistPacket(packetIdentifier, packetPayload);
        return packet;
    }

    //-------------------------------MARK: For deleting-------------------------------
    private void deletePersistableFile(String savedFileName) {
        SimplestValetApp.getContext().deleteFile(savedFileName);
    }
}
