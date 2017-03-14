package io.iqube.healthcaresystem.NotNeeded;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.net.InetAddress;

/**
 * Created by Srinath on 02-03-2017.
 */

public class NSDClass {

    public static String SERVICE_RESOLVED = "Service Resolved";
    private String SERVICE_TYPE = "_workstation._tcp";
    private NsdManager.RegistrationListener mRegistrationListener;
    private String mServiceName;
    private NsdManager mNsdManager;
    private String TAG = "NSD Connection";
    NsdManager.DiscoveryListener mDiscoveryListener;
    NsdManager.ResolveListener mResolveListener;
    NsdServiceInfo mService;
    String mRPiAddress;
    Context context;

    public void Register(Context context){
        mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        mRPiAddress = "";
        initializeDiscoveryListener();
        initializeResolveListener();
        initializeRegistrationListener();
        registerService(8000);
        this.context = context;
        mNsdManager.discoverServices(SERVICE_TYPE,NsdManager.PROTOCOL_DNS_SD,mDiscoveryListener);
    }


    private void registerService(int port) {
        NsdServiceInfo serviceInfo  = new NsdServiceInfo();
        serviceInfo.setServiceName("healthcarephone");
        serviceInfo.setServiceType(SERVICE_TYPE);
        serviceInfo.setPort(port);

        mNsdManager.registerService(
                serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
    }

    private void initializeRegistrationListener() {
        mRegistrationListener = new NsdManager.RegistrationListener() {

            @Override
            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                // Save the service name.  Android may have changed it in order to
                // resolve a conflict, so update the name you initially requested
                // with the name Android actually used.
                mServiceName = NsdServiceInfo.getServiceName();
                InetAddress inetAddress= NsdServiceInfo.getHost();
                Toast.makeText(context,"Service Registered in Network",Toast.LENGTH_SHORT).show();
                int port = NsdServiceInfo.getPort();
                Log.d(TAG,mServiceName+"  "+inetAddress+"  "+port);
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Registration failed!  Put debugging code here to determine why.
                Toast.makeText(context,"Service Registeration Failed",Toast.LENGTH_SHORT).show();
                Log.d(TAG,"Registration Failed");
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0) {
                // Service has been unregistered.  This only happens when you call
                // NsdManager.unregisterService() and pass in this listener.
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Unregistration failed.  Put debugging code here to determine why.
            }
        };
    }

    public void initializeDiscoveryListener() {

        // Instantiate a new DiscoveryListener
        mDiscoveryListener = new NsdManager.DiscoveryListener() {

            //  Called as soon as service discovery begins.
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                // A service was found!  Do something with it.
                String name = service.getServiceName();
                String type = service.getServiceType();
                Log.d("NSD", "Service Name=" + name);
                Log.d("NSD", "Service Type=" + type);
                if (type.equals(SERVICE_TYPE) && name.contains("healthcare")) {
                    Log.d("NSD", "Service Found @ '" + name + "'");
                    Toast.makeText(context,"HealthCare Service Discovered",Toast.LENGTH_SHORT).show();
                    mNsdManager.resolveService(service, mResolveListener);
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.
                String name = service.getServiceName();
                String type = service.getServiceType();
                if (type.equals(SERVICE_TYPE) && name.contains("healthcare")) {
                    Log.d("NSD", "Service Lost @ '" + name + "'");
                    Toast.makeText(context,"HealthCare Service Lost",Toast.LENGTH_SHORT).show();
                    mNsdManager.resolveService(service, mResolveListener);
                }

            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped: " + serviceType);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                //mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                // mNsdManager.stopServiceDiscovery(this);
            }
        };
    }

    public void initializeResolveListener() {
        mResolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Called when the resolve fails.  Use the error code to debug.
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Resolve failed" + errorCode);
                
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.e(TAG, "Resolve Succeeded. " + serviceInfo);
                mService= serviceInfo;

                // Port is being returned as 9. Not needed.
                //int port = mServiceInfo.getPort();

                String name = serviceInfo.getServiceName();
                String type = serviceInfo.getServiceType();
                if (type.equals(SERVICE_TYPE) && name.contains("healthcare")) {
                    Log.d("NSD", "Service Resolved '" + name + "'");
                    Toast.makeText(context,"HealthCare Service Resolved",Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(SERVICE_RESOLVED);
                }
            }
        };
    }



}
