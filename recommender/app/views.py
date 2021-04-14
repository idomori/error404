from django.http import JsonResponse, HttpResponse
from django.views.decorators.csrf import csrf_exempt
import json
import os
import requests
import spotipy
from spotipy import oauth2
from spotipy.oauth2 import SpotifyClientCredentials
import urllib.parse as urlparse
from urllib.parse import parse_qs
from scipy import spatial
from numpy import dot
from numpy.linalg import norm


from django.db import connection
# Added to make recommender Import not working for some reason. I installed pandas
import pandas as pd

os.environ['SPOTIPY_CLIENT_ID']='7a03c05c75c04958926b5213cda242f3'
os.environ['SPOTIPY_CLIENT_SECRET']='6d50816451cf45f4a7b09a0188f30fab'




@csrf_exempt
def dummy(request):
    if request.method != 'GET':
        return HttpResponse(status=404)
    pid = request.GET.get("playlist_id")
    print("dummy is called")
    print(pid)
    return JsonResponse({})


@csrf_exempt
def nearest_neighbors(initial_seed, initial_recs):
		# calculate size of input playlist 
	input_size = len(initial_seed["tracks"]) # 10 is length of input dictonary 
	# take the average for each paramater in input playlist 
	temp_avg = 0
	energy_avg = 0
	key_avg = 0
	dance_avg = 0
	valence_avg = 0

	for song in initial_seed["tracks"]: #playlist is the input dictioanary
	    # need to check if the paramter is wanted and if not it just stays at 0 for all songs
	    # if tempo exits
	    temp_avg = temp_avg + song["tempo"]
	    # if energy exits
	    energy_avg = energy_avg + song["energy"]
	    # if key exits
	    key_avg = key_avg + song["key"]
	    # if dance exits
	    dance_avg = dance_avg + song["danceability"]
	    # if valence exits
	    valence_avg = valence_avg + song["valence"]

	temp_avg = temp_avg / input_size
	energy_avg = energy_avg / input_size
	key_avg = key_avg / input_size
	dance_avg = dance_avg / input_size
	valence_avg = valence_avg / input_size
	# vectorize it (A)
	input_vector = [temp_avg, energy_avg, key_avg, dance_avg, valence_avg]
	spotify_list_vectors = [] # this a list of tuples formatted as (track_id, [tempo, energy, key, dance])
	N = len(initial_recs["recommendation"]) # size of spotify_playlist
	# Now for spotify playlist for each song vectorize paramaters
	for song in initial_recs["recommendation"]: # spotify playlist is the the spotify song dictionary 
	    # need to chosoe which paramters to put in
	    vector_temp = [song["tempo"], song["energy"], song["key"], song["danceability"], song["valence"]]
	    tuple_entry = (song["track_name"], song["artist"], song["track_id"], song["artist_id"], song["preview_url"], song["image_url"], song["tempo"], song["energy"], song["key"], song["danceability"], song["valence"], vector_temp)
	    spotify_list_vectors.append(tuple_entry)
	    # vector_temp.clear()
	    #N = N + 1
	    # check to make sure that clearing the vector doesnt clear it in the tuple and thus the spotify list
	# above is prep work
	# tuple of track id and then vector for the N vectors (not A)
	# Get total amount of vectors from spotify (N)
	cosine_list = []
	for i in range(N):
	    # take cosine similraty between A and each vector 1-N
	    # print(input_vector)
	    # print(spotify_list_vectors[i])
	    # print(spotify_list_vectors[i][4])
	    cos_sim = 1 - spatial.distance.cosine(input_vector, spotify_list_vectors[i][11])
            # cos_sim = dot(input_vector, spotify_list_vectors[i][11]) / (norm(input_vector) * norm(spotify_list_vectors[i][11]))
	    # keep track of track_id
	    cosine_tuple = (spotify_list_vectors[i][0], spotify_list_vectors[i][1], spotify_list_vectors[i][2], spotify_list_vectors[i][3], spotify_list_vectors[i][4], spotify_list_vectors[i][5], spotify_list_vectors[i][6], spotify_list_vectors[i][7], spotify_list_vectors[i][8],spotify_list_vectors[i][9], spotify_list_vectors[i][10], cos_sim)
	    # store in a list the rating
	    cosine_list.append(cosine_tuple)
	# sort list by cosine similarity and then sort by high to low (so most similar is cosine_list[0])
	cosine_list.sort(reverse=True, key = lambda x: x[11])
	# above is cosine similarty
	new_list = []
	print("Variable N: ")
	print(N)
	for j in range(0, int(N / 2)):
		# temp_list = []
		temp_dict = {}
		# print(cosine_list[j][0])
		#temp_list.append(cosine_list[j][0])
		#temp_list.append(cosine_list[j][1])
		#temp_list.append(cosine_list[j][2])
		#temp_list.append(cosine_list[j][3])
		temp_dict["track_name"] = cosine_list[j][0]
		temp_dict["artist"] = cosine_list[j][1]
		temp_dict["track_id"] = cosine_list[j][2]
		temp_dict["artist_id"] = cosine_list[j][3]
		temp_dict["preview_url"] = cosine_list[j][4]
		temp_dict["image_url"] = cosine_list[j][5]
		temp_dict["tempo"] = cosine_list[j][6]
		temp_dict["energy"] = cosine_list[j][7]
		temp_dict["key"] = cosine_list[j][8]
		temp_dict["danceability"] = cosine_list[j][9]
		temp_dict["valence"] = cosine_list[j][10]
		new_list.append(temp_dict)
	#return list
	return new_list
	# if paramater is turned off, need to just put the wanted ones in for the spotify vector temp

@csrf_exempt
def playlist_rec_helper(seed_list):
    auth_manager = SpotifyClientCredentials()
    sp = spotipy.Spotify(auth_manager=auth_manager)
    #seed_list = '6Uoy0crNjS4HHThiwX5pGQ'

    playlist_items = ["track_name","artists","track_id", "artist_id", "preview_url", "image_url", "key","tempo","danceability", "energy", "valence"]

    artists = []
    tracks = []
    playlist = sp.playlist_tracks(seed_list)
    for index in playlist["items"]:
        artists.append(index["track"]["album"]["artists"][0]["id"])
        tracks.append(index["track"]["id"])
   
    rec_info = {}
    #all_recs = {}
    #all_recs["recommendation"] = []

    # for i in range(5,len(playlist)+1,5):
    rec = sp.recommendations(seed_tracks = tracks[0:5], limit=100)
    #recommend["track_name"] = rec["tracks"]["track"]
    #for j in range(0,len(rec["tracks"])):
    rec_info["track_name"] = rec["tracks"][0]["name"]
    rec_info["artist"] = rec["tracks"][0]["album"]["artists"][0]["name"]
    rec_info["track_id"] = rec["tracks"][0]["id"]
    rec_info["artist_id"] = rec["tracks"][0]["album"]["artists"][0]["id"]
    rec_info["preview_url"] = rec["tracks"][0]["preview_url"]#null?
    if rec["tracks"][0]["album"]["images"][0]["url"] is not None:
        rec_info["image_url"] = rec["tracks"][0]["album"]["images"][0]["url"]
    else:
        rec_info["image_url"] = None
    rec_feat = sp.audio_features(rec_info["track_id"])[0]
    for feature in playlist_items[6:]:
        rec_info[feature] = rec_feat[feature]
    #all_recs["recommendation"].append(rec_info)

    return rec_info

@csrf_exempt
def track_rec_helper(seed_list):
    auth_manager = SpotifyClientCredentials()
    sp = spotipy.Spotify(auth_manager=auth_manager)
    #seed_list = '6rqhFgbbKwnb9MLmUQDhG6'

    playlist_items = ["track_name","artists","track_id", "artist_id", "preview_url", "image_url", "key","tempo","danceability", "energy", "valence"]
    seed = [seed_list]
    rec_info = {}
    #track = sp.track(seed_list)
    rec = sp.recommendations(seed_tracks = seed, limit = 25)
    rec_info["track_name"] = rec["tracks"][0]["name"]
    rec_info["artist"] = rec["tracks"][0]["album"]["artists"][0]["name"]
    rec_info["track_id"] = rec["tracks"][0]["id"]
    rec_info["artist_id"] = rec["tracks"][0]["album"]["artists"][0]["id"]
    rec_info["preview_url"] = rec["tracks"][0]["preview_url"]#null?
    rec_info["image_url"] = rec["tracks"][0]["album"]["images"][0]["url"]
    rec_feat = sp.audio_features(rec_info["track_id"])[0]
    for feature in playlist_items[6:]:
        rec_info[feature] = rec_feat[feature]

    return rec_info

def getsongs(request):
    if request.method != 'GET':
        return HttpResponse(status=404)
    auth_manager = SpotifyClientCredentials()
    sp = spotipy.Spotify(auth_manager=auth_manager)  
    track_id = '6rqhFgbbKwnb9MLmUQDhG6'  # Dummy Track
    response = {}
    response['songs'] = sp.audio_features([track_id])
    #  response['songs'] = ['Replace Me', 'DUMMY RESPONSE'] 
    return JsonResponse(response)


@csrf_exempt
def postsong(request):

    if request.method != 'POST':
        return HttpResponse(status=404)
    print(request)
    json_data = json.loads(request.body)
    track_id = json_data['track_id']

    auth_manager = SpotifyClientCredentials()
    sp = spotipy.Spotify(auth_manager=auth_manager)
    token = auth_manager.get_access_token()
    

    track_info = {}
    track_info['songs'] = sp.audio_features([track_id])
    
    ## https://api.spotify.com/v1/tracks/{id}
    id = track_info['songs'][0]['id']
    try:
    	response = requests.get(url='https://api.spotify.com/v1/tracks/' + id, 
                              	headers={'Authorization': 'Bearer '	 + token})
    except requests.exceptions.RequestException as e:  # This is the correct syntax
    	print(e)
   ##  print(response.json())

    response = response.json()
    track_info['songs'][0]['artists'] = response['artists']
    track_info['songs'][0]['name'] = response['name']
    
    ## track_id = '6rqhFgbbKwnb9MLmUQDhG6'  # Dummy Track

    return JsonResponse(track_info)
    #return track_rec_helper(track_id)

@csrf_exempt
def getsong(request):

    if request.method != 'GET':
        return HttpResponse(status=404)
    print(request)
    # json_data = json.loads(request.body)
    # track_id = json_data['track_id']
    track_id = request.GET.get("track_id")
    auth_manager = SpotifyClientCredentials()
    sp = spotipy.Spotify(auth_manager=auth_manager)
    token = auth_manager.get_access_token()
    
    
    track_info = {}
    # track_info['tracks'] = sp.audio_features([track_id])
    sp_response = sp.audio_features([track_id])
    track_info["tracks"] = []
    info = {}
    info["danceability"] = sp_response[0]["danceability"]
    info["tempo"] = sp_response[0]["tempo"]
    info["key"] = sp_response[0]["key"]
    info["valence"] = sp_response[0]["valence"]
    info["energy"] = sp_response[0]["energy"]
    info["track_id"] = sp_response[0]["id"]


    track_info["tracks"].append(info)


    
    ## https://api.spotify.com/v1/tracks/{id}
    id = track_info['tracks'][0]['track_id']
    try:
    	response = requests.get(url='https://api.spotify.com/v1/tracks/' + id, 
                              	headers={'Authorization': 'Bearer '	 + token})
    except requests.exceptions.RequestException as e:  # This is the correct syntax
    	print(e)
   ##  print(response.json())

    response = response.json()
    track_info['tracks'][0]['artist'] = response['artists'][0]['name']
    track_info['tracks'][0]['artist_id'] = response['artists'][0]['id']
    track_info['tracks'][0]['track_name'] = response['name']

    # track_info['songs'][0]['artists'] = response['artists']
    # track_info['songs'][0]['name'] = response['name']


    all_recs = {}
    all_recs["recommendation"] = []
    #gets one recommendation for each iteration of loop
    for i in range(0,5):
    	new_rec = track_rec_helper(track_id)
    	all_recs["recommendation"].append(new_rec)
    #track_list = {}
    #track_list["tracks"] = []
    #track_list["tracks"].append(track_info)
    response = {}
    response["result"] = nearest_neighbors(track_info, all_recs)
    return JsonResponse(response)



#Take two at making read playlist function

@csrf_exempt
def read_playlist(request):
    
    if request.method != 'GET':
        return HttpResponse(status=404)
    # json_data = json.loads(request.body)
    # pid = json_data['playlist_id']

    print("READ_PLAYLIST IS BEING CALLED")
    pid = request.GET.get("playlist_id")

    auth_manager = SpotifyClientCredentials()
    sp = spotipy.Spotify(auth_manager=auth_manager)

    

    playlist_items = ["track_name","artists","track_id", "artist_id", "preview_url", "image_url", "key","tempo","danceability", "energy", "valence"]

    #pid = '6Uoy0crNjS4HHThiwX5pGQ'
    playlist_info = {}
    playlist_info["tracks"] = []
    playlist = sp.playlist_tracks(pid)

    
    playlist_tracks = []


    for track in playlist["items"]:
        playlist_feat = {}
	
        playlist_feat["track_name"] = track["track"]["name"]
        playlist_feat["artist"] = track["track"]["album"]["artists"][0]["name"]
        playlist_feat["track_id"] = track["track"]["id"]
        playlist_feat["artist_id"] = track["track"]["album"]["artists"][0]["id"]
        playlist_feat["preview_url"] = track["track"]["preview_url"]
        playlist_feat["image_url"] = track["track"]["album"]["images"][0]["url"]


        playlist_info["tracks"].append(playlist_feat)
        playlist_tracks.append(track["track"]["id"]) # Adding to this to get audio features later

    # In case that the playlist has more than 100 tracks
    max_amount = min(len(playlist_tracks), 100)

    playlist_af = sp.audio_features(playlist_tracks[:max_amount])
    for track_af, track in zip(playlist_af, playlist_info["tracks"]):
        for feature in playlist_items[6:]:
            track[feature] = track_af[feature]



    #rec_info = reco_helper(pid)
    all_recs = {}
    all_recs["recommendation"] = []


    #gets one recommendation for each iteration of loop
    for i in range(0,5):
    	new_rec = playlist_rec_helper(pid)
    	all_recs["recommendation"].append(new_rec)
    response = {}
    response["result"] = nearest_neighbors(playlist_info, all_recs)


    return JsonResponse(response)


@csrf_exempt
def make_rec(json_input):
    if json_input.method != 'GET':
        return HttpResponse(status=404)
    #seed_list = json_input.GET.get("playlist_id")
    json_data = json.loads(json_input.body)
    seed_list = json_data['playlist_id']
    auth_manager = SpotifyClientCredentials()
    sp = spotipy.Spotify(auth_manager=auth_manager)
   
   #seed_list = '6Uoy0crNjS4HHThiwX5pGQ'

    playlist_items = ["track_name","artists","track_id", "artist_id", "preview_url", "image_url", "key","tempo","danceability"]

    artists = []
    tracks = []
    playlist = sp.playlist_tracks(seed_list)
    for index in playlist["items"]:
        artists.append(index["track"]["album"]["artists"][0]["id"])
        tracks.append(index["track"]["id"])
   
    recommend = {}
    rec_info = {}

    for i in range(5,len(playlist)+1,5):
        rec = sp.recommendations(seed_tracks = tracks[i-5:i], limit=25)
        #recommend["track_name"] = rec["tracks"]["track"]
        rec_info["track_name"] = rec["tracks"][0]["name"]
        rec_info["artist"] = rec["tracks"][0]["album"]["artists"][0]["name"]
        rec_info["track_id"] = rec["tracks"][0]["id"]
        rec_info["artist_id"] = rec["tracks"][0]["album"]["artists"][0]["id"]
        rec_info["preview_url"] = rec["tracks"][0]["preview_url"]#null?
        rec_info["image_url"] = rec["tracks"][0]["album"]["images"][0]["url"]
        rec_feat = sp.audio_features(rec_info["track_id"])[0]

        for feature in playlist_items[6:]:
            rec_info[feature] = rec_feat[feature]


    return JsonResponse(rec_info)

#@csrf_exempt
#def create_recommendations(api_results):
#
#    if api_results.method != 'POST':
#        return HttpResponse(status=404)
#    json_data = json.loads(api_results.body)
#
#    track_info = json_data['track_info']
#
#    auth_manager = SpotifyClientCredentials()
#    sp = spotipy.Spotify(auth_manager=auth_manager)
#    token = auth_manager.get_access_token()
#
#    
#
#    track_name = []
#    track_id = []
#    artist = []
#    album = []
#    duration = []
#    popularity = []
#    for items in api_results['tracks']:
#        try:
#            track_name.append(items['name'])
#            track_id.append(items['id'])
#            artist.append(items["artists"][0]["name"])
#            duration.append(items["duration_ms"])
#            album.append(items["album"]["name"])
#            popularity.append(items["popularity"])
#        except TypeError:
#            pass
#         df = pd.DataFrame({ "track_name": track_name, 
#                             "album": album, 
#                             "track_id": track_id,
#                             "artist": artist, 
#                             "duration": duration, 
#                             "popularity": popularity})
#    return df
