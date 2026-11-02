import xml.etree.ElementTree as ET

root = ET.parse(r"D:\26秋\全栈开发\_tools\repo3.xml").getroot()
targets = {"platforms;android-35", "build-tools;35.0.0", "platform-tools"}


def local(tag):
    return tag.rsplit("}", 1)[-1]


def field(node, name):
    for child in node.iter():
        if local(child.tag) == name and child.text:
            return child.text
    return None


for package in root.iter():
    if local(package.tag) != "remotePackage" or package.get("path") not in targets:
        continue
    print("PACKAGE", package.get("path"))
    for archive in package.iter():
        if local(archive.tag) != "archive":
            continue
        print(
            " host", field(archive, "host-os") or "any",
            "size", field(archive, "size"),
            "url", field(archive, "url"),
            "sha", field(archive, "checksum"),
        )
