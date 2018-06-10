import json
import sys
from functools import reduce

MUNICIPALITIES = ["北京市", "上海市", "天津市", "重庆市"]
SPECIAL = ["澳门特别行政区", "香港特别行政区"]


def is_municipality(name):
    return name in MUNICIPALITIES


def is_special(name):
    return name in SPECIAL


def load_skeleton(skeleton_path):
    with open(skeleton_path, encoding='utf-8') as in_file:
        return json.load(in_file)


def build_skeleton(raw_skeleton):
    countries = raw_skeleton['districts']
    unified_countries = []
    for country in countries:
        unified_country = process_country(country)
        unified_countries.append(unified_country)
    return unified_countries


def process_country(country):
    provinces = country['districts']
    unified_districts = process_provinces(provinces)
    return {
        'adcode': country['adcode'],
        'name': country['name'],
        'center': get_center(country),
        'districts': unified_districts
    }


def process_provinces(provinces):
    unified = []
    for province in provinces:
        province_name = province['name']
        if is_municipality(province_name):
            unified.append(process_municipality(province))
        elif is_special(province_name):
            unified.append(process_special(province))
        else:
            unified.append(process_province(province))
    return unified


def process_municipality(municipality):
    districts = municipality['districts']
    districts = reduce(lambda total, current: total + current['districts'], districts, [])
    municipality['districts'] = districts
    unified = process_district(municipality)
    return unified


def process_special(special):
    unified = process_district(special)
    return unified


def process_province(province):
    unified = process_district(province)
    return unified


def inspect_district(district):
    print(district['name'], get_center(district))
    pass


def process_district(district):
    unified_districts = []
    if 'districts' in district:
        sub_districts = district['districts']
        for sub_district in sub_districts:
            unified_sub_district = process_district(sub_district)
            unified_districts.append(unified_sub_district)
    else:
        unified_districts.append({})
    return {
        'adcode': district['adcode'],
        'name': district['name'],
        'center': get_center(district),
        'districts': unified_districts
    }


def get_center(district):
    return [float(x) for x in district['center'].split(',')]


def run(argv):
    skeleton_path = argv[1]
    in_dir = argv[2]
    out_dir = argv[3]
    raw_skeleton = load_skeleton(skeleton_path)
    unified_skeleton = build_skeleton(raw_skeleton)
    with open('{0}/skeleton.json'.format(out_dir), mode='w', encoding='utf-8') as out_file:
        json.dump(unified_skeleton, out_file, indent=4, ensure_ascii=False)


if __name__ == '__main__':
    argv = sys.argv
    run(argv)

# python3 to_geojson.py  /data/raw/skeleton.json /data/raw /data/unified
