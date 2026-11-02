from __future__ import annotations

import os
import shutil
import subprocess
import zipfile
from pathlib import Path

ANDROID = Path(__file__).resolve().parent
PROJECT = ANDROID.parent
H5 = PROJECT / "dist" / "build" / "h5"
SDK = Path(os.environ.get("ANDROID_SDK_ROOT", r"D:\AndroidSdk"))
BUILD_TOOLS = SDK / "build-tools" / os.environ.get("ANDROID_BUILD_TOOLS", "35.0.0")
ANDROID_JAR = SDK / "platforms" / os.environ.get("ANDROID_PLATFORM", "android-35") / "android.jar"
BUILD = ANDROID / "build"
ASSETS = ANDROID / "assets" / "hengyang-uniapp"
OUT = PROJECT / "dist" / "hengyang-uniapp.apk"


def run(args, cwd=None):
    print("+", " ".join(str(item) for item in args))
    subprocess.run([str(item) for item in args], cwd=cwd, check=True)


def prepare():
    if not H5.exists():
        raise SystemExit(f"H5 build not found: {H5}")
    if BUILD.exists():
        shutil.rmtree(BUILD)
    BUILD.mkdir(parents=True)
    if ASSETS.exists():
        shutil.rmtree(ASSETS)
    shutil.copytree(H5, ASSETS)
    print("assets:", sum(1 for path in ASSETS.rglob("*") if path.is_file()), "files")


def build_resources():
    run([
        BUILD_TOOLS / "aapt2.exe", "compile", "--dir", ANDROID / "res",
        "-o", BUILD / "resources.zip",
    ])
    run([
        BUILD_TOOLS / "aapt2.exe", "link",
        "-o", BUILD / "app-unsigned.apk",
        "-I", ANDROID_JAR,
        "--manifest", ANDROID / "AndroidManifest.xml",
        "-A", ANDROID / "assets",
        BUILD / "resources.zip",
    ])


def build_dex():
    classes = BUILD / "classes"
    dex = BUILD / "dex"
    classes.mkdir()
    dex.mkdir()
    run([
        "javac",
        "-encoding", "UTF-8",
        "-source", "8",
        "-target", "8",
        "-classpath", ANDROID_JAR,
        "-d", classes,
        ANDROID / "java" / "com" / "asuka" / "hengyang" / "MainActivity.java",
    ])
    run([
        BUILD_TOOLS / "d8.bat",
        "--lib", ANDROID_JAR,
        "--min-api", "24",
        "--output", dex,
        classes / "com" / "asuka" / "hengyang" / "MainActivity.class",
    ])
    with zipfile.ZipFile(BUILD / "app-unsigned.apk", "a", zipfile.ZIP_DEFLATED) as archive:
        archive.write(dex / "classes.dex", "classes.dex")


def align_and_sign():
    run([
        BUILD_TOOLS / "zipalign.exe", "-f", "-p", "4",
        BUILD / "app-unsigned.apk", BUILD / "app-aligned.apk",
    ])
    keystore = BUILD / "debug.keystore"
    if not keystore.exists():
        run([
            "keytool", "-genkeypair", "-v",
            "-keystore", keystore,
            "-alias", "androiddebugkey",
            "-storepass", "android",
            "-keypass", "android",
            "-dname", "CN=Android Debug,O=Android,C=US",
            "-keyalg", "RSA",
            "-keysize", "2048",
            "-validity", "10000",
        ])
    OUT.parent.mkdir(parents=True, exist_ok=True)
    run([
        BUILD_TOOLS / "apksigner.bat", "sign",
        "--ks", keystore,
        "--ks-pass", "pass:android",
        "--key-pass", "pass:android",
        "--out", OUT,
        BUILD / "app-aligned.apk",
    ])
    run([BUILD_TOOLS / "apksigner.bat", "verify", "--verbose", OUT])
    run([BUILD_TOOLS / "aapt2.exe", "dump", "badging", OUT])


if __name__ == "__main__":
    prepare()
    build_resources()
    build_dex()
    align_and_sign()
    print("APK:", OUT, OUT.stat().st_size, "bytes")
